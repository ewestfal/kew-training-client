package com.westbrain.training.kew;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * Lanches the KEW Training Client application.
 * 
 * @author Eric Westfall
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class })
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
	
	@Bean
	public Filter riceConfigFilter() {
		return new Filter() {
			
			@Override
			public void init(FilterConfig config) throws ServletException {}
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				if (ConfigContext.getCurrentContextConfig() == null) {
					ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
					if (contextClassLoader != null) {
						Config parentConfig = ConfigContext.getConfig(contextClassLoader.getParent());
						if (parentConfig != null) {
							ConfigContext.init(parentConfig);
						}
					}
				}
				chain.doFilter(request, response);
			}
			
			@Override
			public void destroy() {}
		};
	}
	
	
}
