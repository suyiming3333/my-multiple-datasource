package com.sym.multipledatasources.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(basePackages = "com.sym.multipledatasources.mapper",sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig {
	@Primary
	@Bean(name = "test1DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.one")
	public DataSource getDateSource1() {
		return DruidDataSourceBuilder.create().build();
	}


	@Bean(name = "SqlSessionFactory")
	public SqlSessionFactory SqlSessionFactory(@Qualifier("test1DataSource") DataSource test1DataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(test1DataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/**/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("test1DataSource") DataSource test1DataSource) {
		return new DataSourceTransactionManager(test1DataSource);
	}
}
