package com.lagou.task09;

public class React extends Shape {
    private int len;

    public React() {
    }

    public React(int x, int y, int len) {
        super(x, y);
        this.len = len;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public void show() {
        System.out.println(""+len);
    }
}
