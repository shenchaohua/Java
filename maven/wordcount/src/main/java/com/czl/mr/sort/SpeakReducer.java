package com.czl.mr.sort;


import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SpeakReducer extends Reducer<SpeakBean, NullWritable, SpeakBean, NullWritable> {

    @Override
    protected void reduce(SpeakBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable speakBean : values) {
            context.write(key,NullWritable.get());
        }

    }
}
