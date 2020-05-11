package com.cynic.spring_test;

import com.cynic.service.CynicPracticeService;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/4/29 15:10
 */
public class BeanTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Resource resource = new ClassPathResource("bean-test.xml");
        Resource resource1 = new FileSystemResource("bean-test.xml");
//        InputStream inputStream = resource.getInputStream();
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(resource);
        CynicPracticeService cynicPracticeService = (CynicPracticeService) xmlBeanFactory.getBean("cynicPracticeService");
        cynicPracticeService.sayHello();


    }
}
