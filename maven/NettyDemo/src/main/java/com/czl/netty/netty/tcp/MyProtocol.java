package com.czl.netty.netty.tcp;

import java.util.Arrays;

public class MyProtocol {

    private int len;
    private byte[] content;


    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyProtocol{" +
                "len=" + len +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
