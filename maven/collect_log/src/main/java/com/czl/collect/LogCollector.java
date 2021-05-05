package com.czl.collect;

import java.sql.Time;
import java.util.Timer;

public class LogCollector {
    /*
- 定时采集已滚动完毕日志文件
- 将待采集文件上传到临时目录
- 备份日志文件
*/
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new LogTimerCollector(),0,3600);
    }

}
