package com.czl.cloud.service.fallback;

import com.czl.cloud.service.FeignPaymentService;
import org.springframework.stereotype.Component;

@Component
public class FeignFallbackPaymentService implements FeignPaymentService {
    @Override
    public String getPaymentById(Long id) {
        return "ok fallback in service";
    }

    @Override
    public String getPaymentTimeoutById(Long id) {
        return "timeout fallback in service";
    }
}
