package com.czl.mr.comment.step3;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CommentReducer extends Reducer<CommentBean, NullWritable,CommentBean,NullWritable> {
    @Override
    protected void reduce(CommentBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable value : values) {
            context.write(key,NullWritable.get());
        }
    }
}
