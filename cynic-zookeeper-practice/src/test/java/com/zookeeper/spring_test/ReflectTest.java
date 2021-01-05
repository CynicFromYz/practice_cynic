package com.zookeeper.spring_test;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/5/9 15:05
 */
public class ReflectTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("com.zookeeper.spring_test.ReflectTest");
        ReflectTest reflectTest = (ReflectTest) clazz.newInstance();
        reflectTest.sayHello();
    }

    private void sayHello() {
        System.out.println("hello world!");
    }
}
