package com.czl;

import kafka.zk.KafkaZkClient;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.utils.Time;

import java.nio.charset.Charset;

class MyZkSerializer implements ZkSerializer {
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, Charset.forName("UTF-8"));
    }

    public byte[] serialize(Object obj) throws ZkMarshallingError {
        return String.valueOf(obj).getBytes(Charset.forName("UTF-8"));
    }
}

public class zkclient {

    private static final String ZK_STRING = "10.0.0.15:2181";

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(ZK_STRING);
        System.out.println("ZooKeeper session created.");

        zkClient.setZkSerializer(new MyZkSerializer());
        KafkaZkClient zkc = KafkaZkClient.apply(ZK_STRING, JaasUtils.isZkSecurityEnabled(),
                30000, 30000, Integer.MAX_VALUE, Time.SYSTEM,
                "METRIC_GROUP_NAME", "SessionExpireListener");
        System.out.println(zkc.pathExists("/mysql"));

//        zkClient.createPersistent("/test",true);
//
//        Object data = zkClient.readData("/mysql");
//        System.out.println("success create znode: "+data);
    }
}
