package com.czl.mr.comment.step3;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CommentMapper extends Mapper<Text, BytesWritable,CommentBean, NullWritable> {
    @Override
    protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
        String str = new String(value.getBytes());
        String[] lines = str.split("\n");
        for (String line : lines) {
            if(StringUtils.isNotBlank(line)){
                String[] arr = line.split("\t");
                if (arr.length>=9){
                    context.write(new CommentBean(arr[0],arr[1],arr[2],Integer.parseInt(arr[3]),
                            arr[4],arr[5],arr[6],Integer.parseInt(arr[7]),arr[8]),NullWritable.get());
                }
            }
        }

    }
}
