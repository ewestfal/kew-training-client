package com.westbrain.training.kew;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.kuali.rice.core.impl.config.module.CoreConfigurer;
import org.kuali.rice.coreservice.impl.config.CoreServiceConfigurer;
import org.kuali.rice.kew.config.KEWConfigurer;
import org.kuali.rice.kim.config.KIMConfigurer;
import org.kuali.rice.krad.config.KRADConfigurer;
import org.kuali.rice.ksb.messaging.config.KSBConfigurer;
import org.kuali.rice.location.impl.config.LocationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.bitronix.PoolingDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bitronix.tm.resource.jdbc.lrc.LrcXADataSource;

/**
 * Configures and Embedded KEW client.
 * 
 * @author Eric Westfall
 */
@Configuration
public class KewEmbeddedClientConfiguration {
		
	@Autowired
	private DataSource datasource;
	@Autowired
	@Qualifier("nonTxDataSource")
	private DataSource nonTxDatasource;
	@Autowired
	@Qualifier("riceDataSource")
	private DataSource riceDataSource;
	@Autowired
	private TransactionManager jtaTransactionManager;
	@Autowired
	private UserTransaction userTransaction;
	
	@Bean
	@Autowired
	@ConfigurationProperties("rice.datasource")
	public DataSource riceDataSource(TransactionManager jtaTransactionManager) {
		PoolingDataSourceBean dataSource = new PoolingDataSourceBean();
		dataSource.setClassName(LrcXADataSource.class.getName());
		return dataSource;
	}
	
	@Bean
	public CoreConfigurer coreConfigurer() {
		CoreConfigurer configurer = new CoreConfigurer();
		configurer.setDataSource(datasource);
		configurer.setNonTransactionalDataSource(nonTxDatasource);
		configurer.setServerDataSource(riceDataSource);
		configurer.setTransactionManager(jtaTransactionManager);
		configurer.setUserTransaction(userTransaction);		
		return configurer;
	}
	
	@Bean
	@Autowired
	public KSBConfigurer ksbConfigurer(CoreConfigurer coreConfigurer) {
		return new KSBConfigurer();
	}	
	
	@Bean
	@Autowired
	public KRADConfigurer kradConfigurer(CoreConfigurer coreConfigurer) {
		return new KRADConfigurer();
	}

	@Bean
	@Autowired
	public CoreServiceConfigurer coreServiceConfigurer(CoreConfigurer coreConfigurer) {
		return new CoreServiceConfigurer();
	}

	@Bean
	@Autowired
	public LocationConfigurer locationConfigurer(CoreConfigurer coreConfigurer) {
		return new LocationConfigurer();
	}
	
	@Bean
	@Autowired
	public KIMConfigurer kimConfigurer(CoreConfigurer coreConfigurer) {
		return new KIMConfigurer();
	}
	
	@Bean
	@Autowired
	public KEWConfigurer kewConfigurer(CoreConfigurer coreConfigurer) {
		return new KEWConfigurer();
	}
	
}
