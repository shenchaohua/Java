package com.czl.mr.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {
    Text text = new Text();
    IntWritable one = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] strings = str.split(" ");
        for (String s : strings) {
            text.set(s);
            context.write(text,one);
        }
    }
}
