package com.westbrain.training.kew;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * Lanches the KEW Training Client application.
 * 
 * @author Eric Westfall
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException {
		new SpringApplicationBuilder(Application.class).listeners(new StartupListener()).run(args);
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
