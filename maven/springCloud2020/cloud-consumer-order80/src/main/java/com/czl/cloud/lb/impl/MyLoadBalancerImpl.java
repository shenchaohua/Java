package com.czl.cloud.lb.impl;

import com.czl.cloud.lb.MyLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalancerImpl implements MyLoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger();

    private final int getIndex() {
        int current;
        int next;
        for (;;) {
            current = atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
            if (atomicInteger.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> instanceList) {
        int index = getIndex() % instanceList.size();
        return instanceList.get(index);
    }
}
