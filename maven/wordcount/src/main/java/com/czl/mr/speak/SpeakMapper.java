package com.czl.mr.speak;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SpeakMapper extends Mapper<LongWritable, Text,Text,SpeakBean> {
    Text text = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String s = value.toString();
        String[] strings = s.split("\t");
        text.set(strings[1]);
        context.write(text,new SpeakBean(Long.parseLong(strings[strings.length-3]),Long.parseLong(strings[strings.length-2]),strings[1]));
    }
}
