package com.westbrain.training.kew;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class BackdoorFilter extends org.springframework.security.web.authentication.switchuser.SwitchUserFilter {
	
	private static final String BACKDOOR_ID = "backdoorId";
	
	@Autowired
	public BackdoorFilter(UserDetailsService userDetailsService) {
		setUsernameParameter(BACKDOOR_ID);
		setUserDetailsService(userDetailsService);
		setSuccessHandler(new NoOpAuthenticationSuccessHandler());
	}
	
	@Override
	protected boolean requiresSwitchUser(HttpServletRequest request) {
		return request.getParameter(BACKDOOR_ID) != null;
	}
	
	
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// check for switch or exit request
		if (requiresSwitchUser(request)) {
			// if set, attempt switch and store original
			try {
				Authentication targetUser = attemptSwitchUser(request);

				// update the current context to the new target user
				SecurityContextHolder.getContext().setAuthentication(targetUser);
			}
			catch (AuthenticationException e) {
				logger.debug("Switch User failed", e);
			}
		}
		else if (requiresExitUser(request)) {
			// get the original authentication object (if exists)
			Authentication originalUser = attemptExitUser(request);

			// update the current context back to the original user
			SecurityContextHolder.getContext().setAuthentication(originalUser);
		}

		chain.doFilter(request, response);
	}

	private static final class NoOpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			// do nothing if authentication was successful, we just want to proceed
		}
		
	}
	
}
