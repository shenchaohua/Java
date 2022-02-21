package com.czl.cloud.controller;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.lb.MyLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class OrderController {

    public static final String URL = "http://CLOUD-PAYMENT-SERVICE";

    public static final String SERVER_ID = "CLOUD-PAYMENT-SERVICE";

    @Resource
    public MyLoadBalancer myLoadBalancer;

    @Resource
    public DiscoveryClient discoveryClient;

    @Resource
    public RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult create(Payment payment) {
        return restTemplate.postForObject(URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable Long id) {
        List<ServiceInstance> instances = discoveryClient.getInstances(SERVER_ID);
        if (instances.size() ==0) {
            return null;
        }
        ServiceInstance instance = myLoadBalancer.getInstance(instances);
        return restTemplate.getForObject(instance.getUri() + "/payment/get/"+id, CommonResult.class);
    }
}
