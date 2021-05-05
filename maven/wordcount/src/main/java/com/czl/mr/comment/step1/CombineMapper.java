package com.czl.mr.comment.step1;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CombineMapper extends Mapper<LongWritable, Text, NullWritable, BytesWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        BytesWritable bytesWritable = new BytesWritable();
        byte[] bytes = value.getBytes();
        bytesWritable.set(bytes,0,bytes.length);
        context.write(NullWritable.get(),bytesWritable);
    }
}
