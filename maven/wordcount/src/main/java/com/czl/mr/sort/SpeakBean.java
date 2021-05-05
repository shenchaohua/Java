package com.czl.mr.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SpeakBean implements WritableComparable<SpeakBean> {
    private Long selfDuration;
    private long thirdDuration;
    private String deviceId;
    private long sumDuration;

    public SpeakBean() {
    }

    public SpeakBean(Long selfDuration, long thirdDuration, String deviceId) {
        this.selfDuration = selfDuration;
        this.thirdDuration = thirdDuration;
        this.deviceId = deviceId;
        this.sumDuration = selfDuration+thirdDuration;
    }



    @Override
    public String toString() {
        return "SpeakBean{" +
                "selfDuration=" + selfDuration +
                ", thirdDuration=" + thirdDuration +
                ", deviceId='" + deviceId + '\'' +
                ", sumDuration=" + sumDuration +
                '}';
    }

    public Long getSelfDuration() {
        return selfDuration;
    }

    public void setSelfDuration(Long selfDuration) {
        this.selfDuration = selfDuration;
    }

    public long getThirdDuration() {
        return thirdDuration;
    }

    public void setThirdDuration(long thirdDuration) {
        this.thirdDuration = thirdDuration;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getSumDuration() {
        return sumDuration;
    }

    public void setSumDuration(long sumDuration) {
        this.sumDuration = sumDuration;
    }

    @Override
    public int compareTo(SpeakBean o) {
        if (sumDuration > o.getSumDuration()){
            return -1;
        }
        else if (sumDuration<o.getSumDuration()){
            return 1;
        }
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(selfDuration);
        dataOutput.writeLong(thirdDuration);
        dataOutput.writeUTF(deviceId);
        dataOutput.writeLong(sumDuration);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.selfDuration = dataInput.readLong();
        this.thirdDuration = dataInput.readLong();
        this.deviceId = dataInput.readUTF();
        this.sumDuration = dataInput.readLong();
    }
}
