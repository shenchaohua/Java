package com.czl.netty.nio;

import jdk.nashorn.internal.runtime.RewriteException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class ByteBufferFileChannelDemo {

    public  static void read() throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream("./read.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello".getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

    public static void write() throws IOException {
        File file = new File("./read.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);
        fileInputStream.close();
        System.out.println(new String(byteBuffer.array()));
    }

    public static void copy() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("./read.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("./copy.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        while (true) {
            buffer.clear();     //如果不执行clear，write完毕的时候，limit和position相等，就不会尝试读取。结果=0
            int read = fileInputStreamChannel.read(buffer);
            if (read == -1) {
                break;
            }
            buffer.flip();
            fileOutputStreamChannel.write(buffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void copy2() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("./read.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("./copy.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        fileOutputStreamChannel.transferFrom(fileInputStreamChannel, 0, fileInputStreamChannel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void mapped () throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("./read.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 3);

        mappedByteBuffer.put(0, (byte) 'H');

        randomAccessFile.close();
    }

    public static void main(String[] args) throws IOException {
//        read();
//        write();
//        copy();
        mapped();
    }
}
