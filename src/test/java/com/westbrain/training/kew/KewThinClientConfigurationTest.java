package com.westbrain.training.kew;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.Config;

public class KewThinClientConfigurationTest {

	private KewThinClientConfiguration config = new KewThinClientConfiguration();
	
	@Test
	public void testConfigFactory() throws Exception {
		config.setKeystoreAlias("myalias");
		config.setKeystorePassword("mypassword");
		config.setKeystoreFile("/path/to/my/keystore");
		config.setRiceServerUrl("http://localhost:8080");
		Config riceConfig = config.configFactory();
		
		assertEquals("myalias", riceConfig.getKeystoreAlias());
		assertEquals("myalias", riceConfig.getProperty("keystore.alias"));
		assertEquals("kew-training-client", riceConfig.getProperty("application.id"));
		assertEquals(RunMode.THIN.toString(), riceConfig.getProperty("kim.mode"));
		assertEquals(RunMode.THIN.toString(), riceConfig.getProperty("kew.mode"));
		assertEquals(RunMode.THIN.toString(), riceConfig.getProperty("ksb.mode"));
		
		// check some of the properties that should be brought in from common-config-defaults.xml
		assertEquals("dev", riceConfig.getEnvironment());
		assertEquals("kr", riceConfig.getProperty("app.code"));
		
	}
	
}
