package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		final User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " cannot be found!"));
		user.updateRolesAndPermissions();

		return user;
	}
}
