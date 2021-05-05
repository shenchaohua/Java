package com.czl.mr.speak;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SpeakBean implements Writable {
    private Long selfDuration;
    private long thirdDuration;
    private String deviceId;
    private long sumDuration;

    public SpeakBean() {

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
        this.setSelfDuration(dataInput.readLong());
        this.setThirdDuration(dataInput.readLong());
        this.setDeviceId(dataInput.readUTF());
        this.setSumDuration(dataInput.readLong());
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

    public SpeakBean(Long selfDuration, long thirdDuration, String deviceId) {
        this.selfDuration = selfDuration;
        this.thirdDuration = thirdDuration;
        this.deviceId = deviceId;
        this.sumDuration = selfDuration  + thirdDuration;
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
}
