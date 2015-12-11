package com.westbrain.training.kew;

import java.io.IOException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class })
public class Application {

	public static void main(String[] args) throws IOException {
		new SpringApplicationBuilder(Application.class).listeners(new StartupListener()).run(args);
	}

	@Bean
	public TrackingPostProcessor trackingPostProcessor() {
		return new TrackingPostProcessor();
	}
	
	/**
	 * Loads Kuali Rice configuration on startup, after Spring Environment has been prepared.
	 */
	static class StartupListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

		@Override
		public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
			RiceConfigInjector.injectDefaults(event.getEnvironment());
		}		
		
	}
	
}
