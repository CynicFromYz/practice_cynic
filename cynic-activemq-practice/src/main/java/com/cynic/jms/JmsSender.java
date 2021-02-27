package com.cynic.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsSender {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("" + "tcp://106.12.50.184:61616");
        Connection connection = null;
        try {
            //创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            //创建队列 (如果队列已经创建则不会创建 first-queue是队列名称)
            Destination destination = session.createQueue("first-queue");
            //创建消息发送者
            MessageProducer producer = session.createProducer(destination);
            TextMessage textMessage = session.createTextMessage("hello acs.");
            producer.send(textMessage);
            session.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
