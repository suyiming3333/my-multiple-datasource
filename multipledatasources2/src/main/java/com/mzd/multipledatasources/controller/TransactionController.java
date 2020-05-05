package com.mzd.multipledatasources.controller;

import java.util.UUID;

import com.mzd.multipledatasources.service.ServiceComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mzd.multipledatasources.bean.TeachersBean;
import com.mzd.multipledatasources.bean.TestBean;
import com.mzd.multipledatasources.dao.test02.TransactionDao2;
import com.mzd.multipledatasources.service.TransactionService1;
import com.mzd.multipledatasources.service.TransactionService2;

/**
 * 多数据源事务测试
 * 
 * @author acer
 *
 */
@RestController
public class TransactionController {
	@Autowired
	private TransactionService1 ts1;
	@Autowired
	private TransactionService2 ts2;

	@Autowired
	private ServiceComponent serviceComponent;

	@RequestMapping("/savetest.do")
	public String savetest() {
		TestBean tb = new TestBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setScore(70);
		tb.setClassid("1");
		tb.setUserid("a");
		ts1.savetestBean(tb);
		return "success";
	}

	@RequestMapping("/saveteacher.do")
	public String saveteacher() {
		TeachersBean tb = new TeachersBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setTeachername("王老师");
		tb.setClassid("1");
		ts2.saveTeacher(tb);
		return "success";
	}

	// ########################开始事务测试##########################
	/**
	 * 结果是一个插入进去了，属于非正常现象
	 * 
	 * @return
	 */
	@RequestMapping("/test.do")
	public String test() {
		TestBean tb = new TestBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setScore(70);
		tb.setClassid("1");
		tb.setUserid("a");
		ts1.savetestBean2(tb);
		return "success";
	}

	/**
	 * 结果是两个都没法插入---属于正常现象
	 * 
	 * @return
	 */
	@RequestMapping("/test2.do")
	public String test2() {
		TestBean tb = new TestBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setScore(70);
		tb.setClassid("1");
		tb.setUserid("a");
		ts1.savetestBean3(tb);
		return "success";
	}

	/**
	 * 结果是一个插入进去了，属于非正常现象
	 * 
	 * @return
	 */
	@RequestMapping("/test3.do")
	public String test3() {
		TestBean tb = new TestBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setScore(70);
		tb.setClassid("1");
		tb.setUserid("a");
		ts1.savetestBean4(tb);
		return "success";
	}

	@RequestMapping("/doSomething")
	public String doSomething(){
		serviceComponent.doSomething();
		return "success";
	}

	@RequestMapping("/doServiceOneWithException")
	public String doServiceOneWithException(){
		TestBean tb = new TestBean();
		tb.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		tb.setScore(70);
		tb.setClassid("1");
		tb.setUserid("a");
		ts1.savetestBean(tb);
		return "success";
	}

	@RequestMapping("/doServiceTwoWithException")
	public String doServiceTwoWithException(){
		TeachersBean teachersBean = new TeachersBean();
		teachersBean.setClassid("10088");
		teachersBean.setId("1233");
		teachersBean.setTeachername("装载机昂老是");
		ts2.saveTeacher2(teachersBean);
		return "success";
	}
}
