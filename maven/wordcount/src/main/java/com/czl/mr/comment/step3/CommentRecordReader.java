package com.czl.mr.comment.step3;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class CommentRecordReader extends RecordWriter<CommentBean, NullWritable> {
    private FSDataOutputStream out;
    public CommentRecordReader(FSDataOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(CommentBean commentBean, NullWritable nullWritable) throws IOException, InterruptedException {
        out.write(commentBean.toString().getBytes());
        out.write("\n".getBytes());
        out.flush();
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeStream(out);
    }
}
