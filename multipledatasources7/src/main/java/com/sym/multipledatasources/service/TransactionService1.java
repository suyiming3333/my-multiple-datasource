package com.sym.multipledatasources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sym.multipledatasources.bean.TestBean;
import com.sym.multipledatasources.dao.TransactionDao1;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService1 {
	@Autowired
	private TransactionDao1 ts1;

	@Transactional
	public void test01_saveTestBean(TestBean t) {
		ts1.test01save(t);
		throw new RuntimeException("运行时异常");
	}

}
