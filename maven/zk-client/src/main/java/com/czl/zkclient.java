package com.czl;

import org.I0Itec.zkclient.ZkClient;

public class zkclient {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("worker1:2181");
        System.out.println("ZooKeeper session created.");
        String data = zkClient.readData("/mysql");
        System.out.println("success create znode."+data);
    }
}
