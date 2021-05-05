package com.czl.mr.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SpeakDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(SpeakDriver.class);
        job.setMapperClass(SpeakMapper.class);
        job.setReducerClass(SpeakReducer.class);
        job.setMapOutputKeyClass(SpeakBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(SpeakBean.class);
        job.setOutputValueClass(NullWritable.class);
        FileInputFormat.setInputPaths(job,"F:\\output\\part-r-00000");
        FileOutputFormat.setOutputPath(job,new Path("F:\\output\\sort"));

        System.exit(job.waitForCompletion(true)?0:1);
    }
}
