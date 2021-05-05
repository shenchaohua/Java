package com.czl.mr.comment.step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class CustomRecordReader extends RecordReader {
    FileSplit split;
    Configuration configuration;
    boolean isProgress =true;
    private BytesWritable value = new BytesWritable();
    private Text k = new Text();
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.split =(FileSplit)inputSplit;
        configuration = taskAttemptContext.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (isProgress) {
            byte[] contents = new byte[(int) split.getLength()];
            FileSystem fs;
            FSDataInputStream fis;
            Path path = split.getPath();
            fs = path.getFileSystem(configuration);
            fis = fs.open(path);
            IOUtils.readFully(fis, contents, 0, contents.length);
            value.set(contents, 0, contents.length);
            String name = split.getPath().toString();
            k.set(name);
            isProgress = false;
            IOUtils.closeStream(fis);
            return true;		//	下一次需要返回false
        }
        return false;
    }

    @Override
    public Object getCurrentKey() throws IOException, InterruptedException {
        return k;
    }

    @Override
    public Object getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
