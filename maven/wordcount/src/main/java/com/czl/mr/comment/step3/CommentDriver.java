package com.czl.mr.comment.step3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

public class CommentDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(CommentDriver.class);
        job.setMapperClass(CommentMapper.class);
        job.setReducerClass(CommentReducer.class);
        job.setMapOutputKeyClass(CommentBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(CommentBean.class);
        job.setOutputValueClass(NullWritable.class);
        job.setPartitionerClass(CommentPartitioner.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(CommentOutputFormat.class);
        FileInputFormat.setInputPaths(job,new Path("f:/output-merge2"));
        FileOutputFormat.setOutputPath(job,new Path("f:/output"));
        job.setNumReduceTasks(3);
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
