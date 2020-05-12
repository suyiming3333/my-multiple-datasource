package com.sym.multipledatasources.service;

import com.sym.multipledatasources.bean.TeachersBean;
import com.sym.multipledatasources.bean.TestBean;
import com.sym.multipledatasources.dao.TransactionDao1;
import com.sym.multipledatasources.dao.TransactionDao2;
import com.sym.multipledatasources.datasource.MultiTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: ServiceComponent
 * @Package com.sym.multipledatasources.service
 * @Description: TODO
 * @date 2020/5/5 15:11
 */

@Service
public class ServiceComponent {


    @Autowired
    private TransactionService1 transactionService1;

    @Autowired
    private TransactionService2 transactionService2;

    @Autowired
    private TransactionDao1 transactionDao1;

    @Autowired
    private TransactionDao2 transactionDao2;

    /**
     * 分布式事务的问题(一)
     */
    @Transactional(rollbackFor = ArithmeticException.class)
    public void doSomething() {
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
        int i2 = 1 / 0;

    }


    /**
     * @Transactional 只会处理主数据源事务，导致不同数据库事务都提交都同一个事务，两个记录会插到同一个库
     * 需要引入jtaTransactionManager 处理
     */
//    @Transactional(rollbackFor = ArithmeticException.class)
    public void XATransationTest() {

        TestBean testBean = new TestBean();
        testBean.setClassid("10086");
        testBean.setId("122");
        testBean.setScore(99);
        testBean.setUserid("10000");

        TeachersBean teachersBean = new TeachersBean();
        teachersBean.setClassid("10088");
        teachersBean.setId("1233");
        teachersBean.setTeachername("装载机昂老是");


        /**
         *  谁先执行，按谁的事务走；transactionService2没有事务支持，2/1事务都不会回滚且事务会提交到前者的数据库
         */
        transactionService1.test01_saveTestBean(testBean);
//        transactionService2.test02_saveTeachersBean(teachersBean);
//        int i2 = 1/0;

    }


    /**
     * 手动处理多数据源的事务
     */
//    public void XATransationTest2() {
//
//        TestBean testBean = new TestBean();
//        testBean.setClassid("10086");
//        testBean.setId("122");
//        testBean.setScore(99);
//        testBean.setUserid("10000");
//
//        TeachersBean teachersBean = new TeachersBean();
//        teachersBean.setClassid("10088");
//        teachersBean.setId("1233");
//        teachersBean.setTeachername("装载机昂老是");
//
//        UserTransaction tran = jtaTransactionManager.getUserTransaction();
//        try {
//            tran.begin();
//            transactionService1.test01_saveTestBean(testBean);
//            transactionService2.test02_saveTeachersBean(teachersBean);
//            int i = 1/0;//此处会抛异常，service1,service2事务都会回滚
//            tran.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                tran.rollback();
//            } catch (SystemException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }


    /**
     * 注解实现多数据源，事务管理
     */
    @MultiTransactional(rollbackFor = RuntimeException.class)
    public void XATransationTest3(){

        TestBean testBean = new TestBean();
        testBean.setClassid("10086");
        testBean.setId("122");
        testBean.setScore(99);
        testBean.setUserid("10000");

        TeachersBean teachersBean = new TeachersBean();
        teachersBean.setClassid("10088");
        teachersBean.setId("1233");
        teachersBean.setTeachername("装载机昂老是");

        transactionService1.test01_saveTestBean(testBean);
        transactionService2.test02_saveTeachersBean(teachersBean);
//        int i = 1/0;//此处会抛异常，service1,service2事务都会回滚
    }
//这里注释开启后，会导致事务出错，数据都提交到一个库
//    @Transactional(transactionManager = "transactionManager")
    public void doDiffrentTx(){
        TestBean testBean = new TestBean();
        testBean.setClassid("10086");
        testBean.setId("122");
        testBean.setScore(99);
        testBean.setUserid("10000");

        TeachersBean teachersBean = new TeachersBean();
        teachersBean.setClassid("10088");
        teachersBean.setId("1233");
        teachersBean.setTeachername("装载机昂老是");
        transactionDao1.test01save(testBean);
        transactionDao2.test02save(teachersBean);
//        int i = 1/0;
    }
}
