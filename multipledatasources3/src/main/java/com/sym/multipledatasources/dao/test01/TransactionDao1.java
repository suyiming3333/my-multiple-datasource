package com.sym.multipledatasources.dao.test01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sym.multipledatasources.bean.TestBean;
import com.sym.multipledatasources.mapper.test01.TransactionMapping1;

@Component

public class TransactionDao1 {
	@Autowired
	private TransactionMapping1 tm1;

	public void save(TestBean t) {
		tm1.save(t);
//		int i2 = 1/0;
	}

}
