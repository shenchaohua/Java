package com.lagou.task10;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    public String value() default "hello";
}
