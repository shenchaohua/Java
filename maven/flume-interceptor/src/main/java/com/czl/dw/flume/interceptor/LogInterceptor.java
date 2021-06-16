package com.czl.dw.flume.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.utils.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String eventBody = new String(event.getBody(), Charsets.UTF_8);
// 获取 event 的 header
        Map<String, String> headersMap = event.getHeaders();
// 解析body获取json串
        String[] bodyArr = eventBody.split("\\s+");
        try{
            String jsonStr = bodyArr[6];
// 解析json串获取时间戳
            String timestampStr = "";
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            if (headersMap.getOrDefault("logtype", "").equals("start")){
// 取启动日志的时间戳
                timestampStr = jsonObject.getJSONObject("app_active").getString("time");
            } else if (headersMap.getOrDefault("logtype", "").equals("event")) {
// 取事件日志第一条记录的时间戳
                JSONArray jsonArray = jsonObject.getJSONArray("lagou_event");
                if (jsonArray.size() > 0){
                    timestampStr = jsonArray.getJSONObject(0).getString("time");
                }
            }
// 将时间戳转换为字符串 "yyyy-MM-dd"
// 将字符串转换为Long
            long timestamp = Long.parseLong(timestampStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Instant instant = Instant.ofEpochMilli(timestamp);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                    ZoneId.systemDefault());
            String date = formatter.format(localDateTime);
// 将转换后的字符串放置header中
            headersMap.put("logtime", date);
            event.setHeaders(headersMap);
        }catch (Exception e){
            headersMap.put("logtime", "Unknown");
            event.setHeaders(headersMap);
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> lstEvent = new ArrayList<>();
        for (Event event: events){
            Event outEvent = intercept(event);
            if (outEvent != null) {
                lstEvent.add(outEvent);
            }
        }
        return lstEvent;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new LogInterceptor();
        }
        @Override
        public void configure(Context context) {
        }
    }
}
