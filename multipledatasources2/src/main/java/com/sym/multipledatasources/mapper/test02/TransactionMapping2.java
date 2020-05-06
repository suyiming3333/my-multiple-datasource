package com.sym.multipledatasources.mapper.test02;

import org.springframework.stereotype.Repository;

import com.sym.multipledatasources.bean.TeachersBean;

@Repository
public interface TransactionMapping2 {

	void save(TeachersBean t);

}
