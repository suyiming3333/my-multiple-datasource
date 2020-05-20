package com.sym.multipledatasources.mq;

import com.sym.multipledatasources.bean.TeachersBean;
import com.sym.multipledatasources.service.TransactionService1;
import com.sym.multipledatasources.service.TransactionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import java.util.UUID;

@Component
public class TopicListener {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private TransactionService2 transactionService2;

    /**
     * 通过消息队列的监听创建teacherc
     * @JmsListener中未指定containerFactory，那么将使用默认配置，默认配置中Session是开启了事务的
     * @param message
     */
    @Transactional
    @JmsListener(destination = "test-queue1")
    public void recieve(String message){
        System.out.println("recieve message :"+message);
        TeachersBean tb = new TeachersBean();
        tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        tb.setTeachername(message);
        tb.setClassid("1");
        transactionService2.test02_saveTeachersBean(tb);
        jmsTemplate.convertAndSend("test-queue-reply",message+" created");
//        throw new RuntimeException("run time");//异常后mq会重试7次，后进入死信队列(Dead LetterQueue)
    }
}
