package com.czl.mr.speak;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
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
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(SpeakBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(SpeakBean.class);
        FileInputFormat.setInputPaths(job,args[0]);
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        System.exit(job.waitForCompletion(true)?0:1);
    }
}
