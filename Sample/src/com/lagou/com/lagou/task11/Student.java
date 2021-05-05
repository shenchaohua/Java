package com.lagou.com.lagou.task11;

import java.io.FilenameFilter;
import java.util.Objects;

public class Student extends Person {
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name);
    }

    public void test(){

    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
