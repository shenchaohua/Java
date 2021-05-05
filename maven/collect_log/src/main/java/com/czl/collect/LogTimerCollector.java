package com.czl.collect;

import com.czl.Common.Const;
import com.czl.utils.PropertiesTool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

public class LogTimerCollector extends TimerTask {

    @Override
    public void run() {
    /*
    - 定时采集已滚动完毕日志文件
    - 将待采集文件上传到临时目录
    - 备份日志文件
    */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(new Date());

        Properties prop = PropertiesTool.getProperties();

        File file = new File(prop.getProperty(Const.LOG_DIR));
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String property = prop.getProperty(Const.LOG_PREFIX);
                return pathname.getName().startsWith(property);
            }
        });
        File tmpPath = new File(prop.getProperty(Const.LOG_TMP_DIR)+dayStr);
        if(!tmpPath.exists()){
            tmpPath.mkdirs();
        }
        for (File one : files) {
            one.renameTo(new File(tmpPath.getPath()+"/"+one.getName()));
        }
        File[] listFiles = tmpPath.listFiles();
        Configuration configuration = new Configuration();
        FileSystem fileSystem = null;
        File bakPath = new File(prop.getProperty(Const.LOG_BK_DIR)+dayStr);
        if(!bakPath.exists()){
            bakPath.mkdirs();
        }
        try {
            fileSystem = FileSystem.get(new URI("hdfs://worker1:9000"), configuration, "root");
            Path savePath = new Path(prop.getProperty(Const.LOG_SAVE_DIR)+dayStr);
            if (!fileSystem.exists(savePath)){
                fileSystem.mkdirs(savePath);
            }
            for (File listFile : listFiles) {
                fileSystem.copyFromLocalFile(new Path(listFile.getPath()),new Path(prop.getProperty(Const.LOG_SAVE_DIR)+dayStr+"/"+listFile.getName()));
                listFile.renameTo(new File(bakPath.getPath()+"/"+listFile.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
