package com.westbrain.training.kew;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.bitronix.PoolingDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import bitronix.tm.resource.jdbc.lrc.LrcXADataSource;

/**
 * Configures the applications XA and Non-XA datasources.
 * 
 * @author Eric Westfall
 */
@Configuration
public class DatabaseConfiguration {
		
	@Bean
	@Autowired
	@Primary
	@ConfigurationProperties("datasource")
	public DataSource dataSource(TransactionManager jtaTransactionManager) {
		PoolingDataSourceBean dataSource = new PoolingDataSourceBean();
		dataSource.setClassName(LrcXADataSource.class.getName());
		return dataSource;
	}

	@Bean
	@ConfigurationProperties("nonTxDatasource")
	public DataSource nonTxDataSource() {
		return DataSourceBuilder.create().build();
	}
	
}
