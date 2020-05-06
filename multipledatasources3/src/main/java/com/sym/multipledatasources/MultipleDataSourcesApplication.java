package com.sym.multipledatasources;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.sym.multipledatasources.datasource.Datasource1;
import com.sym.multipledatasources.datasource.Datasource2;

@SpringBootApplication
// @EnableConfigurationProperties注解使@ConfigurationProperties注解生效
@EnableConfigurationProperties(value = { Datasource1.class, Datasource2.class })
@MapperScan("com.sym.multipledatasources.mapper")
public class MultipleDataSourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDataSourcesApplication.class, args);
	}
}
