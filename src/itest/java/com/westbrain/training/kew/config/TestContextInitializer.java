package com.westbrain.training.kew.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.westbrain.training.kew.RiceConfigInjector;

public class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		RiceConfigInjector.injectDefaults(applicationContext.getEnvironment());		
	}

}
