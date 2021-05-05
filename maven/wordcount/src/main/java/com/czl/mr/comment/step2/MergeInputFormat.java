package com.czl.mr.comment.step2;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class MergeInputFormat extends FileInputFormat {
    @Override
    public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        CustomRecordReader recordReader = new CustomRecordReader();
        recordReader.initialize(inputSplit,taskAttemptContext);
        return recordReader;
    }
}
