package com.czl.utils;

import com.czl.collect.LogTimerCollector;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;
import java.util.Properties;

public class PropertiesTool {

    public static Properties prop = null;

    public static Properties getProperties(){
        if(prop==null){
            synchronized ("this"){
                if(prop==null){
                    prop = new Properties();
                    try {
                        prop.load(LogTimerCollector.class.getClassLoader().getResourceAsStream("properties"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return prop;
    }

}
