package com.zookeeper;

import com.zookeeper.service.CynicPracticeService;
import com.zookeeper.springcontext.ContextUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CynicCommonPracticeApplicationTests {


    @Test
    void contextLoads() {
        CynicPracticeService cynicPracticeService = ContextUtil.getBean(CynicPracticeService.class);
        cynicPracticeService.sayHello();
    }

}
