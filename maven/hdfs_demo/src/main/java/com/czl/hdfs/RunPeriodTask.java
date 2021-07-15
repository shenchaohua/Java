package com.czl.hdfs;

import org.apache.hadoop.conf.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;
import org.apache.hadoop.fs.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.fs.FileSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    private static FileSystem fileSystem = null;
    private final static int sleepTimeInSecond = 1000*60*20;


    public static void initFileSystem() throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        fileSystem = FileSystem.get(new URI("hdfs://worker1:9000"), configuration, "root");
    }

    public static boolean CheckHdfsPathExists(String hdfsPath) throws IOException {
        return fileSystem.exists(new Path(hdfsPath));
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
        File file = Paths.get(shellPath,taskName).toFile();
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        String line;
        StringBuilder shellContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            shellContent.append(line).append('\n');
        }
        reader.close();
        Pattern p_for_name= Pattern.compile("check_exist \\$\\{(\\w+)}");
        Matcher m_for_name=p_for_name.matcher(shellContent);
        ArrayList<String> hdfsPath = new ArrayList<>();
        while(m_for_name.find()) {
            String name = m_for_name.group(1);
            Pattern p_for_path= Pattern.compile(name + "=\"(\\S+)(dt|date)=\\$");
            Matcher m_for_path = p_for_path.matcher(shellContent);
            if(!m_for_path.find()){
                System.out.println("task: "+ taskName + "path wrong");
                return hdfsPath;
            }
            hdfsPath.add(m_for_path.group(1));
        }
        return hdfsPath;
    }

    public static ArrayList<Date> getDateList(){
        ArrayList<Date> dateList = new ArrayList<>();
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        for(int i=1;i<=30;i++){
            calendar.add(Calendar.DAY_OF_MONTH,-i);
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

    public static boolean checkThisDateHadRunTask(String taskName,String date){
        File rootPath = new File(shellPath);
        if(!rootPath.exists()){
            rootPath.mkdirs();
        }
        File targetPath = Paths.get(shellPath, taskName, date).toFile();
        return targetPath.exists();
    }
    public static void saveTaskRunSuccess(String taskName,String date){
        File targetPath = Paths.get(shellPath, taskName, date).toFile();
        targetPath.mkdirs();
    }
    public static void main(String[] args) throws Exception {
        initFileSystem();
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
                        // 检查是否已经跑过，以及是否hdfs路径存在
                        if(!checkThisDateHadRunTask(taskName,dateString) && CheckHdfsPathExists(hdfsPath+dateString)){
                            // 2。检查服务器路径下是否有该路径执行过的标识，选择是否执行该shell
                            boolean success = execShell(taskName);
                            // 3.如果执行成功，写入本地标识
                            // 执行失败，跳过写入，下一次继续尝试
                            if(success){
                                saveTaskRunSuccess(taskName,dateString);
                            }
                        }
                    }
                }
            }
            System.out.println("start sleeping time: "+sleepTimeInSecond + "s");
            Thread.sleep(1000L *sleepTimeInSecond);
        }
    }
    /**
     * HandlerTest Class
     */

}