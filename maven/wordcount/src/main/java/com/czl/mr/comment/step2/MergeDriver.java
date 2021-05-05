package com.czl.mr.comment.step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

public class MergeDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(MergeDriver.class);
        job.setMapperClass(MergeMapper.class);
        job.setReducerClass(MergeReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        job.setInputFormatClass(MergeInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.RECORD);
        SequenceFileOutputFormat.setOutputCompressorClass(job, DefaultCodec.class);
        FileInputFormat.setInputPaths(job,new Path("f:/output-merge"));
        FileOutputFormat.setOutputPath(job,new Path("f:/output-merge2"));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
