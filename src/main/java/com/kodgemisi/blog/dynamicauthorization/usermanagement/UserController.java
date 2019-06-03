package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import com.kodgemisi.blog.dynamicauthorization.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
class UserController {

	private final UserRepository userRepository;

	private final UserService userService;

	private final RoleRepository roleRepository;

	@GetMapping
	String index(Model model) {

		model.addAttribute("users", userRepository.findAll());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("newUser", new User());

		return "usermanagement/users";
	}

	@PostMapping
	String index(User user) {

		userService.createUser(user);

		return "redirect:/users";
	}

	@DeleteMapping("/{userId}/roles/{roleName}")
	@ResponseBody
	ResponseEntity<Void> removeRole(@PathVariable("userId") Long userId, @PathVariable("roleName") String roleName) {

		userService.removeRole(userId, roleName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/{userId}/roles/{roleName}")
	@ResponseBody
	ResponseEntity<Void> addRole(@PathVariable("userId") Long userId, @PathVariable("roleName") String roleName) {

		userService.addRole(userId, roleName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}

@Service
@RequiredArgsConstructor
class UserService {

	private final RoleRepository roleRepository;

	private final UserRepository userRepository;

	void createUser(User user) {

		final Set<Role> rolesWithPermissions = new HashSet<>();

		for (Role role : user.getRoles()) {
			Role roleWithPermissions = roleRepository.findById(role.getName()).orElseThrow(
					() -> new IllegalStateException("Something sinister is going on, user sent a non-existing role!"));
			rolesWithPermissions.add(roleWithPermissions);
		}

		user.setRoles(rolesWithPermissions);
		user.updateRolesAndPermissions();

		userRepository.save(user);
	}

	@Transactional
	public void removeRole(Long userId, String roleName) {
		final User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
		user.removeRole(roleName);
	}

	@Transactional
	public void addRole(Long userId, String roleName) {
		final User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
		user.addRole(roleName);
	}

}