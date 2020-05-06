package com.mzd.multipledatasources.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.SystemException;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@MapperScan(basePackages = "com.mzd.multipledatasources.mapper", sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig {
	@Bean(name = "test1DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.test1")
	public DataSource getDateSource1() {
		return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
	}

	@Primary
	@Bean(name = "test2DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.test2")
	public DataSource getDateSource2() {
		return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
	}

	@Bean(name = "dynamicDataSource")
	public DynamicDataSource DataSource(@Qualifier("test1DataSource") DataSource test1DataSource,
			@Qualifier("test2DataSource") DataSource test2DataSource) throws SQLException {
		Map<Object, Object> targetDataSource = new HashMap<>();
//		targetDataSource.put(DataSourceType.DataBaseType.TEST01, initXADataSource(test1DataSource,"test1DataSource"));
//		targetDataSource.put(DataSourceType.DataBaseType.TEST02, initXADataSource(test2DataSource,"test2DataSource"));
		targetDataSource.put(DataSourceType.DataBaseType.TEST01, test1DataSource);
		targetDataSource.put(DataSourceType.DataBaseType.TEST02, test2DataSource);

		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSource);
		dataSource.setDefaultTargetDataSource(test1DataSource);
		return dataSource;
	}

	@Bean(name = "SqlSessionFactory")
	public SqlSessionFactory test1SqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dynamicDataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*.xml"));
		return bean.getObject();
	}


	public DataSource initXADataSource(DataSource ds,String resourceName) throws SQLException {
		MysqlXADataSource mysqlxadatasource = new MysqlXADataSource();

		String url = ds.getConnection().getMetaData().getURL();
		String password = "";
		String username = ds.getConnection().getMetaData().getUserName();
		mysqlxadatasource.setUrl(url);
		mysqlxadatasource.setPassword(password);
		mysqlxadatasource.setUser("root");
		mysqlxadatasource.setPinGlobalTxToPhysicalConnection(true);
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlxadatasource);
		xaDataSource.setUniqueResourceName(resourceName);
		return xaDataSource;
	}


    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() {
        UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
        atomikosTransactionManager.setForceShutdown(true);
        return atomikosTransactionManager;
    }

    @Bean(name = "atomikosUserTransaction")
    public UserTransactionImp atomikosUserTransaction() {
        UserTransactionImp atomikosUserTransaction = new UserTransactionImp();
        try {
            atomikosUserTransaction.setTransactionTimeout(300);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return atomikosUserTransaction;
    }

    /***
     * jta事务管理器配置
     * @param atomikosTransactionManager
     * @param atomikosUserTransaction
     * @return
     */
    @Bean(name = "transactionManager")
    public JtaTransactionManager jtaTransactionManager(UserTransactionManager atomikosTransactionManager,
                                                    UserTransactionImp atomikosUserTransaction) {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(atomikosTransactionManager);
        transactionManager.setUserTransaction(atomikosUserTransaction);
        transactionManager.setAllowCustomIsolationLevels(true);
        return transactionManager;
    }

}
