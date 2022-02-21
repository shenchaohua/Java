package com.czl.cloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface MyLoadBalancer {

    ServiceInstance getInstance(List<ServiceInstance> instanceList);

}
