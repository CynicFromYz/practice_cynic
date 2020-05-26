package com.zookeeper.controller;

import com.zookeeper.CountDownTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/20 14:50
 */
@Controller
@RequestMapping("/test")
public class CountController {

    @ResponseBody
    @RequestMapping("/count")
    public Object test() throws InterruptedException {
        System.out.println("请求进入=============================");
        CountDownTest.countDownLatch.countDown();
        CountDownTest.countDownLatch.await();
        System.out.println("===============================================================================");
        return "11";
    }
}
