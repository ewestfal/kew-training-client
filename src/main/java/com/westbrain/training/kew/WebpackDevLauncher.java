package com.westbrain.training.kew;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Executes a webpack dev server with hot reload capabilities on localhost port
 * 3000.
 * 
 * <p>
 * In order for this environment to run properly, an "npm install" must have
 * already been executed from the command line. Additionally, "npm" needs to be
 * available on the path in the appropriate command shell environment for the
 * given OS (/bin/bash on *nix and cmd on Windows).
 * 
 * <p>
 * Is only loaded when the "dev" profile is present.
 * </p>
 * 
 * @author Eric Westfall
 */
@Configuration
@Profile("dev")
public class WebpackDevLauncher {

	private static final String DEFAULT_HOST = "0.0.0.0";
	private static final String DEFAULT_PORT = "3000";
	private static final String WEBPACK_CMD = "node_modules/.bin/webpack-dev-server --hot --inline --host "
			+ DEFAULT_HOST + " --port " + DEFAULT_PORT + " --output.publicPath=http://" + DEFAULT_HOST + ":"
			+ DEFAULT_PORT + "/";

	@Bean
	WebpackRunner webpackDevRunner() {
		return new WebpackRunner();
	}

	@Bean
	ServletRegistrationBean bundleServlet() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new BundleServlet(), "/bundle.js");
		return servlet;
	}

	public static class WebpackRunner implements InitializingBean, DisposableBean {

		private static final String[] WIN_DEV_SERVER_COMMAND = { "cmd", "-c", WEBPACK_CMD };
		private static final String[] NIX_DEV_SERVER_COMMAND = { "/bin/bash", "-l", "-c", WEBPACK_CMD };

		private Process process;

		@Override
		public void afterPropertiesSet() throws Exception {
			process = startWebpackDevServer(isWindows() ? WIN_DEV_SERVER_COMMAND : NIX_DEV_SERVER_COMMAND);
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
			return new ProcessBuilder(command).inheritIO().start();
		}

		private boolean isWindows() {
			return System.getProperty("os.name").toLowerCase().contains("windows");
		}

	}

	public static class BundleServlet extends HttpServlet {

		private static final long serialVersionUID = 7934943629760606723L;

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			URL obj = new URL("http://" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/bundle.js");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			Enumeration<String> headerNames = req.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				con.setRequestProperty(headerName, req.getHeader(headerName));
			}
			
			resp.setStatus(con.getResponseCode());						

			resp.setContentType("application/javascript");
			ServletOutputStream sout = resp.getOutputStream();

			InputStream inputStream = con.getInputStream();
			int data;
			while ((data = inputStream.read()) != -1) {
				sout.write(data);
			}
			
		}

	}

}
