package com.sym.multipledatasources.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * JMS配置 是mq事务与db事务同步
 */

@Configuration
public class JmsConfig {
    @Bean
    public ConnectionFactory connectionFactory(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        TransactionAwareConnectionFactoryProxy proxy = new TransactionAwareConnectionFactoryProxy();
        proxy.setTargetConnectionFactory(connectionFactory);//设置连接工厂
        proxy.setSynchedLocalTransactionAllowed(true);//同步到本地的事务
        return proxy;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);//启动事务
        return jmsTemplate;
    }
}
