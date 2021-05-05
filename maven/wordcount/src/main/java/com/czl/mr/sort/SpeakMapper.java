package com.czl.mr.sort;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SpeakMapper extends Mapper<LongWritable, Text, SpeakBean, NullWritable> {
    Text text = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String s = value.toString();
        String[] strings = s.split("\t");
        text.set(strings[1]);
        context.write(new SpeakBean(Long.parseLong(strings[strings.length-4]),Long.parseLong(strings[strings.length-3]),strings[0]),NullWritable.get());
    }
}
