package com.cynic.spring_test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/4/29 15:10
 */
public class BeanTest {
    public static void main(String[] args) throws IOException {
        Resource resource = new ClassPathResource("bean-test.xml");
        InputStream inputStream = resource.getInputStream();

    }
}
