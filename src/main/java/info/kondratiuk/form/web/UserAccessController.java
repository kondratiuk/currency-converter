/*
 * Copyright (c) 2015 O.K. GNU LGPL v3.
 *
 */
package info.kondratiuk.form.web;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import info.kondratiuk.form.model.User;
import info.kondratiuk.form.model.UserDaoImpl;

/**
 * Controller for the User Access.
 * 
 * @author o.k.
 */
public class UserAccessController implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		logger.info("Load User By email: " + email);

		String emailTmp = "";
		String passwordTmp = "";
		boolean isUserFound = false;
		List<User> users = UserDaoImpl.getAllUsers();
		
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email)) {
				emailTmp = user.getEmail();
				passwordTmp = user.getPassword();
				isUserFound = true;
				continue;
			}
		}
		final User user = new User();
		user.setEmail(emailTmp);
		user.setPassword(passwordTmp);

		if (!isUserFound) {
			throw new UsernameNotFoundException(email + " not found!");
		}

		return new UserDetails() {
			private static final long serialVersionUID = 5546869192664791949L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public String getUsername() {
				return user.getEmail();
			}

			@Override
			public String getPassword() {
				return user.getPassword();
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
				return authorities;
			}
		};
	}
}
