package com.czl.mr.comment.step3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.w3c.dom.Text;

import java.io.IOException;

public class CommentOutputFormat extends FileOutputFormat<CommentBean, NullWritable> {
    @Override
    public RecordWriter<CommentBean, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        Configuration configuration = taskAttemptContext.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        String path = configuration.get("mapreduce.output.fileoutputformat.outputdir");
        int id = taskAttemptContext.getTaskAttemptID().getTaskID().getId();
        FSDataOutputStream out = null;
        if(id==0){
            out = fileSystem.create(new Path(path + "/good/good.log"));
        }else if(id==1){
            out = fileSystem.create(new Path(path + "/common/common.log"));
        }else{
            out = fileSystem.create(new Path(path + "/bad/bad.log"));
        }
        return new CommentRecordReader(out);
    }
}
