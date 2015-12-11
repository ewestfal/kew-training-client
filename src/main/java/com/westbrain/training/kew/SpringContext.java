package com.westbrain.training.kew;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * A class which has a static method to allow for access of the application's
 * Spring context. This is useful when dealing with legacy code or code that is
 * not managed by Spring but which needs access to the context.
 * 
 * @author Eric Westfall
 */
@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext context) {
		SpringContext.context = context;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

}
