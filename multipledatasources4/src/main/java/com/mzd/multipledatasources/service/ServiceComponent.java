package com.mzd.multipledatasources.service;

import com.mzd.multipledatasources.bean.TeachersBean;
import com.mzd.multipledatasources.bean.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: ServiceComponent
 * @Package com.mzd.multipledatasources.service
 * @Description: TODO
 * @date 2020/5/5 15:11
 */

@Service
public class ServiceComponent {


    @Autowired
    private TransactionService1 transactionService1;

    @Autowired
    private TransactionService2 transactionService2;

    /**
     * 分布式事务的问题(一)
     */
    @Transactional(rollbackFor = ArithmeticException.class)
    public void doSomething(){
        TestBean testBean = new TestBean();
        testBean.setClassid("10086");
        testBean.setId("122");
        testBean.setScore(99);
        testBean.setUserid("10000");
        //假如service1处理出错，程序不往下执行
//        transactionService1.test01_saveTestBean(testBean);
//        int i = 1/0;
        TeachersBean teachersBean = new TeachersBean();
        teachersBean.setClassid("10088");
        teachersBean.setId("1233");
        teachersBean.setTeachername("装载机昂老是");
        //假如service2出错了，service1事务已经提交 无法回滚
        transactionService2.test02_saveTeachersBean(teachersBean);
        transactionService1.test01_saveTestBean(testBean);

/*
        Transactional 对于这个异常，spring @Transactional下，同一时刻，只能对service1进行回滚(只负责当前数据源的事务)，
        service2无法回滚
*/
        int i2 = 1/0;

    }
}
