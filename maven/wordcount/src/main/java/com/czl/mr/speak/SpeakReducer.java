package com.czl.mr.speak;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.xerces.dom.TextImpl;

import java.io.IOException;

public class SpeakReducer extends Reducer<Text,SpeakBean,Text,SpeakBean> {
    Long selfDuration = 0L;
    Long thirdDuration = 0L;

    @Override
    protected void reduce(Text key, Iterable<SpeakBean> values, Context context) throws IOException, InterruptedException {
        for (SpeakBean speakBean : values) {
            selfDuration += speakBean.getSelfDuration();
            thirdDuration += speakBean.getThirdDuration();
        }
        context.write(key,new SpeakBean(selfDuration,thirdDuration,key.toString()));
    }
}
