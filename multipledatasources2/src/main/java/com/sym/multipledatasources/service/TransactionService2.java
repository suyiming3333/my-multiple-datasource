package com.sym.multipledatasources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.sym.multipledatasources.bean.TeachersBean;
import com.sym.multipledatasources.dao.test02.TransactionDao2;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class TransactionService2 {
	@Autowired
	private TransactionDao2 ts2;

	@Autowired
	private PlatformTransactionManager secondaryTransactionManager;

//	@Transactional
	public void saveTeacher(TeachersBean t) {

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = secondaryTransactionManager.getTransaction(def);
		try{
			ts2.save(t);
			secondaryTransactionManager.commit(status);
		}catch (Exception e){
			secondaryTransactionManager.rollback(status);
			e.printStackTrace();
		}
	}

	@Transactional
	public void saveTeacher2(TeachersBean t) {
		int i = 1 / 0;
		ts2.save(t);
	}
}
