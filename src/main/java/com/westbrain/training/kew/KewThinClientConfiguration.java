package com.westbrain.training.kew;

import java.io.IOException;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.config.property.SimpleConfig;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.kuali.rice.kew.config.KEWConfigurer;
import org.kuali.rice.kim.config.KIMConfigurer;
import org.kuali.rice.ksb.messaging.config.KSBConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KewThinClientConfiguration {
	
	@Value("${rice.keystore.file}")
	private String keystoreFile;
	@Value("${rice.keystore.alias}")
	private String keystoreAlias;
	@Value("${rice.keystore.password}")
	private String keystorePassword;
	@Value("${rice.standaloneServerUrl}")
	private String riceServerUrl;

	@Bean
	public Config configFactory() throws IOException {
		
		SimpleConfig simpleConfig = new SimpleConfig();
		simpleConfig.putProperty("ksb.mode", RunMode.THIN.toString());
		simpleConfig.putProperty("kim.mode", RunMode.THIN.toString());
		simpleConfig.putProperty("kew.mode", RunMode.THIN.toString());		
		simpleConfig.putProperty("application.id", "kew-training-client");
		simpleConfig.putProperty("keystore.file", keystoreFile);
		simpleConfig.putProperty("keystore.alias", keystoreAlias);
		simpleConfig.putProperty("keystore.password", keystorePassword);
		simpleConfig.putProperty("rice.server.url", riceServerUrl);
		
		Config xmlConfig = new JAXBConfigImpl("classpath:META-INF/common-config-defaults.xml", simpleConfig);
		xmlConfig.parseConfig();
		
		ConfigContext.init(xmlConfig);
		return ConfigContext.getCurrentContextConfig();		
	}
	
	@Bean
	@Autowired
	public KSBConfigurer ksbConfigurer(Config config) {
		return new KSBConfigurer();
	}
	
	@Bean
	@Autowired
	public KIMConfigurer kimConfigurer(Config config) {
		return new KIMConfigurer();
	}
	
	@Bean
	@Autowired
	public KEWConfigurer kewConfigurer(Config config) {
		return new KEWConfigurer();
	}
	
}
