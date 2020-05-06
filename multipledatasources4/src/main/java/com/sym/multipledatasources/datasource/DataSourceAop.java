package com.sym.multipledatasources.datasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sym.multipledatasources.datasource.DataSourceType.DataBaseType;

@Aspect
@Component
public class DataSourceAop {
	@Before("execution(* com.sym.multipledatasources.service..*.test01*(..))")
	public void setDataSource2test01() {
		System.err.println("test01业务");
		DataSourceType.setDataBaseType(DataBaseType.TEST01);
	}

	@After("execution(* com.sym.multipledatasources.service..*.test01*(..))")
	public void clearDataSource2test01() {
//		System.err.println("test01业务");
		DataSourceType.clearDataBaseType();
	}
	
	@Before("execution(* com.sym.multipledatasources.service..*.test02*(..))")
	public void setDataSource2test02() {
		System.err.println("test02业务");
		DataSourceType.setDataBaseType(DataBaseType.TEST02);
	}

	@After("execution(* com.sym.multipledatasources.service..*.test02*(..))")
	public void clearDataSource2test02() {
//		System.err.println("test02业务");
		DataSourceType.clearDataBaseType();
	}
}
