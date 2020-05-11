package com.sym.multipledatasources.mapper;

import org.springframework.stereotype.Repository;

import com.sym.multipledatasources.bean.TestBean;

@Repository
public interface TransactionMapping1 {

	void save(TestBean t);

}
