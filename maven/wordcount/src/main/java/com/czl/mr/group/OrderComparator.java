package com.czl.mr.group;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class OrderComparator extends WritableComparator {
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        return super.compare(a, b);
    }
}
