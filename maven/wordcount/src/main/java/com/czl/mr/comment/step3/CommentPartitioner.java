package com.czl.mr.comment.step3;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class CommentPartitioner extends Partitioner<CommentBean, NullWritable> {

    @Override
    public int getPartition(CommentBean commentBean, NullWritable nullWritable, int numReduceTasks) {
        return (commentBean.getCommentStatus() & 2147483647) % numReduceTasks;
    }
}
