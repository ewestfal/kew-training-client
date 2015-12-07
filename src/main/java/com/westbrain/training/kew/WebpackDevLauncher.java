package com.westbrain.training.kew;

import java.io.IOException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Executes a webpack dev server with hot reload capabilities on localhost port 3000.
 * 
 * <p>In order for this environment to run properly, an "npm install" must have already been
 * executed from the command line. Additionally, "npm" needs to be available on the path
 * in the appropriate command shell environment for the given OS (/bin/bash on *nix and cmd on Windows).
 * 
 * <p>Is only loaded when the "dev" profile is present.</p>
 * 
 * @author Eric Westfall
 */
@Configuration
@Profile("dev")
public class WebpackDevLauncher {

	@Bean
	WebpackRunner webpackDevRunner() {
		return new WebpackRunner();
	}
	
	public static class WebpackRunner implements InitializingBean, DisposableBean {
		
		private static final String[] WIN_DEV_SERVER_COMMAND = {"cmd", "-c", "node_modules/.bin/webpack-dev-server --hot --inline --host 0.0.0.0 --port 3000 --output.publicPath=http://localhost:3000/"};
		private static final String[] NIX_DEV_SERVER_COMMAND = {"/bin/bash", "-l", "-c", "node_modules/.bin/webpack-dev-server --hot --inline --host 0.0.0.0 --port 3000 --output.publicPath=http://localhost:3000/"};
		private static final String WEBPACK_SERVER_PROPERTY = "webpack-server-loaded";

		
		private Process process;

		@Override
		public void afterPropertiesSet() throws Exception {
			if (System.getProperty(WEBPACK_SERVER_PROPERTY) == null) {
				process = startWebpackDevServer(isWindows() ? WIN_DEV_SERVER_COMMAND : NIX_DEV_SERVER_COMMAND);
			}
		}

		@Override
		public void destroy() throws Exception {
			if (process != null) {
				process.getOutputStream().close();
				process.getInputStream().close();
				process.getErrorStream().close();
				process.destroyForcibly();
				process.waitFor();
			}
		}

		private Process startWebpackDevServer(String[] command) throws IOException {
			Process process = new ProcessBuilder(command).inheritIO().start();			
			System.setProperty(WEBPACK_SERVER_PROPERTY, "true");
			return process;
		}
		
		private boolean isWindows() {
			return System.getProperty("os.name").toLowerCase().contains("windows");
		}

	}


}
