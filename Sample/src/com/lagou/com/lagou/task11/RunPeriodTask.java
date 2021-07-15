package com.lagou.com.lagou.task11;

import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunPeriodTask {
    public static SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat format_2 = new SimpleDateFormat("yyyyMMdd");
    private final static HashMap<String, SimpleDateFormat> taskMap = new HashMap<>();
    {
        taskMap.put("gen_app_converse_billing.sh",format_1);
        taskMap.put("gen_dsp_billing.sh",format_1);
        taskMap.put("gen_sdk_game_billing.sh",format_1);
        taskMap.put("gen_search_billing.sh",format_1);
        taskMap.put("gen_yingyongbao_billing.sh",format_1);
    }
    private static String shellPath;
    private static String storagePath = "/root/crontab/spark/";


    public static boolean CheckHdfsPathExists(String hdfsPath){
//        Configuration configuration = new Configuration();
        return true;
    }

    public static boolean execShell(String taskName) throws IOException, InterruptedException {
        String cmd = "sh " + taskName;
        Process process = Runtime.getRuntime().exec(cmd, null, new File(shellPath));
        int status = process.waitFor();
        if(status != 0){
            System.err.println("Failed to call shell's command and the return status's is: " + status);
            return false;
        }
        return true;
    }


    public static ArrayList<String> parseHdfsPath(String taskName) throws IOException {
        Path shellFilePath = Paths.get(shellPath,taskName);
        File file = shellFilePath.toFile();
        BufferedReader reader = new BufferedReader(new FileReader(file.getName()));
        String line;
        StringBuilder shellContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            shellContent.append(line).append('\n');
        }
        reader.close();
        Pattern p_for_name= Pattern.compile("check_exist \\$\\{([a-z]+)}");
        Matcher m_for_name=p_for_name.matcher(shellContent);
        ArrayList<String> hdfsPath = new ArrayList<>();
        while(m_for_name.find()) {
            String name = m_for_name.group();
            Pattern p_for_path= Pattern.compile(name + "=\"(\\S+(dt|date)=$)");
            Matcher m_for_path = p_for_path.matcher(shellContent);
            if(m_for_path.groupCount()==0){
                System.out.println("task: "+ taskName + "path wrong");
                return hdfsPath;
            }
            hdfsPath.add(m_for_path.group());
        }
        return hdfsPath;
    }

    public static boolean checkTaskCanRun(String taskName) throws IOException {
        ArrayList<String> hdfsPath = parseHdfsPath(taskName);
        for (String onePath : hdfsPath) {
            if(!CheckHdfsPathExists(onePath)){
                System.out.println("task: "+ taskName + " path: "+onePath+" do not exits!");
                System.out.println("Ignore task:" + taskName);
                return false;
            }
        }
        return true;
    }
    public static ArrayList<Date> getDateList(){
        ArrayList<Date> dateList = new ArrayList<>();
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();

        for(int i=1;i<=30;i++){
            calendar.add(Calendar.DAY_OF_MONTH,i);
            dateList.add(calendar.getTime());
        }
        return dateList;
    }
    public static boolean checkThisDateHadRunTask(String taskName,String date){
        File rootPath = new File(storagePath);
        if(!rootPath.exists()){
            rootPath.mkdirs();
        }
        File targetPath = Paths.get(storagePath, taskName, date).toFile();
        return targetPath.exists();
    }
    public static void saveTaskRunSuccess(String taskName,String date){
        File targetPath = Paths.get(storagePath, taskName, date).toFile();
        targetPath.mkdirs();
    }
    public static void main(String[] args) throws Exception {
        if(args.length!=2){
            throw new Exception("Must pass shell path!");
        }
        shellPath = args[1];
        for(;;) {
            ArrayList<Date> dateList = getDateList();
            for (String taskName : taskMap.keySet()) {
                // 1。获取该task对应hdfs哪些路径最近一个月上传的，返回子目录列表，形式如date=xxxx-xx-xx
                SimpleDateFormat format = taskMap.get(taskName);
                ArrayList<String> hdfsPaths = parseHdfsPath(taskName);
                for (String hdfsPath : hdfsPaths) {
                    for (Date date : dateList) {
                        String dateString = format.format(date);
                        if(!checkThisDateHadRunTask(taskName,dateString) && CheckHdfsPathExists(hdfsPath+dateString)){
                            // 2。检查服务器路径下是否有该路径执行过的标识，选择是否执行该shell
                            boolean success = execShell(taskName);
                            // 如果执行成功，写入本地标识
                            //执行失败，跳过写入，下一次继续尝试
                            if(success){
                                saveTaskRunSuccess(taskName,dateString);
                            }
                        }
                    }
                }
            }
            Thread.sleep(1000*60*20);
        }
    }

    /**
     * HandlerTest Class
     */

}