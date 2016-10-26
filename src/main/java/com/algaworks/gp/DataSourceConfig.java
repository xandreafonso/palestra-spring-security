package com.algaworks.gp;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dmds = new DriverManagerDataSource();
		dmds.setDriverClassName("com.mysql.jdbc.Driver");
		dmds.setUrl("jdbc:mysql://localhost:3306/gerenciamentoprojetos");
		dmds.setUsername("root");
		//dmds.setPassword("123");
		return dmds;
	}

}
