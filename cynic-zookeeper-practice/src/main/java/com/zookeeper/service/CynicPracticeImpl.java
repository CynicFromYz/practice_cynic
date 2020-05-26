package com.zookeeper.service;

import org.springframework.stereotype.Service;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/4/29 15:16
 */
@Service
public class CynicPracticeImpl implements CynicPracticeService{
    @Override
    public void sayHello() {
        System.out.println("hello world!");
    }
}
