package com.westbrain.training.kew;

import org.apache.commons.lang3.StringUtils;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KimUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("no username provided");
		}
		Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(username);
		if (principal == null) {
			throw new UsernameNotFoundException(username + " could not be found.");
		}
		return new User(principal.getPrincipalName(), principal.getPrincipalName(), AuthorityUtils.createAuthorityList("ROLE_USER"));
	}
	
}
