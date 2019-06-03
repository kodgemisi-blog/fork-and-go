package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Component
@RequiredArgsConstructor
class GenerateSampleData implements CommandLineRunner {

	private final BCryptPasswordEncoder encoder;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Override
	public void run(String... args) {

		User jane = new User("Jane Doe", "jane@kodgemisi.com", encoder.encode("123"));
		User john = new User("John Doe", "john@kodgemisi.com", encoder.encode("123"));

		permissionRepository.save(new Permission("books:list"));
		permissionRepository.save(new Permission("books:edit"));
		permissionRepository.save(new Permission("books:create"));
		permissionRepository.save(new Permission("books:read"));
		permissionRepository.save(new Permission("books:delete"));

		Role admin = Role.ADMIN;
		admin.addPermission("books:list");
		admin.addPermission("books:read");
		admin.addPermission("books:edit");
		admin.addPermission("books:create");
		admin.addPermission("books:delete");
		roleRepository.save(admin);

		Role reader = Role.READER;
		reader.addPermission("books:list");
		reader.addPermission("books:read");
		roleRepository.save(reader);

		jane.addRole(admin);
		john.addRole(reader);

		userRepository.save(jane);
		userRepository.save(john);
	}
}
