package com.lagou.task15;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FileTest {
    public static void main(String[] args) {
        File file = new File("./a.txt");
        if(file.exists()){
            System.out.println(file.getName());
            System.out.println(file.length());
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println(dateTimeFormatter.format(dateTime));
            System.out.println(file.getAbsolutePath());
            System.out.println(file.delete());
        }else{
            try {
                System.out.println(file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
