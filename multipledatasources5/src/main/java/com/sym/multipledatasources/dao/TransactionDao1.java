package com.sym.multipledatasources.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sym.multipledatasources.bean.TestBean;
import com.sym.multipledatasources.mapper.TransactionMapping1;

@Component

public class TransactionDao1 {
	@Autowired
	private TransactionMapping1 tm1;

	public void test01save(TestBean t) {
		tm1.save(t);
	}

}
