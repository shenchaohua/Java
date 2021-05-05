package com.czl.mr.comment.step1;

import com.czl.mr.comment.step2.MergeDriver;
import com.czl.mr.comment.step2.MergeInputFormat;
import com.czl.mr.comment.step2.MergeMapper;
import com.czl.mr.comment.step2.MergeReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.jboss.netty.channel.ChannelHandlerLifeCycleException;

import java.io.IOException;

public class CombineDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(CombineDriver.class);
        job.setMapperClass(CombineMapper.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(BytesWritable.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job,1024*1024*4);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.RECORD);
        SequenceFileOutputFormat.setOutputCompressorClass(job, DefaultCodec.class);

        FileInputFormat.setInputPaths(job,new Path("f:/input"));
        FileOutputFormat.setOutputPath(job,new Path("f:/output-merge"));

        System.exit(job.waitForCompletion(true)?0:1);
    }
}
