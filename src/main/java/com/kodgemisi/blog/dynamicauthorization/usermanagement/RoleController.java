package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import com.kodgemisi.blog.dynamicauthorization.ResourceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
class RoleController {

	private final RoleRepository roleRepository;

	private final PermissionRepository permissionRepository;

	private final RoleService roleService;

	@GetMapping
	String index(Model model) {

		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("permissions", permissionRepository.findAll());
		model.addAttribute("newRole", new Role());

		return "usermanagement/roles";
	}

	@PostMapping
	String index(RoleDto roleDto) {

		roleRepository.save(roleDto.toRole());

		return "redirect:/roles";
	}

	@DeleteMapping("/{roleName}")
	String delete(@PathVariable("roleName") String roleName) {

		roleRepository.deleteById(roleName);

		return "redirect:/roles";
	}

	@DeleteMapping("/{roleName}/permissions/{permissionName}")
	@ResponseBody
	ResponseEntity<Void> deletePermission(@PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName) {

		roleService.removePermission(roleName, permissionName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/{roleName}/permissions/{permissionName}")
	@ResponseBody
	ResponseEntity<Void> addPermission(@PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName) {

		roleService.addPermission(roleName, permissionName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}

@Service
@RequiredArgsConstructor
class RoleService {

	private final RoleRepository roleRepository;

	@Transactional
	public void removePermission(String roleName, String permissionName) {
		final Role role = roleRepository.findById(roleName).orElseThrow(ResourceNotFoundException::new);
		role.removePermission(permissionName);
	}

	@Transactional
	public void addPermission(String roleName, String permissionName) {
		final Role role = roleRepository.findById(roleName).orElseThrow(ResourceNotFoundException::new);
		role.addPermission(permissionName);
	}
}

/**
 * {@link RoleDto#permissions} should be a {@link java.util.List} instead of a {@link java.util.Set} otherwise Spring MVC doesn't auto-bind checkbox items.
 * That's why we use a dto for {@link com.kodgemisi.blog.dynamicauthorization.usermanagement.Role} class.
 */
@Getter
@Setter
class RoleDto {

	private String name;

	private List<Permission> permissions = new ArrayList<>();

	Role toRole() {
		Role role = new Role(this.name);

		// When **only** `permissions[2].name: books:edit` is selected then there will be 2 permission objects with null names in index 0 and 1.
		// Remove permissions with null names
		permissions.removeAll(Collections.singleton(Permission.EMPTY));

		role.setPermissions(new HashSet<>(permissions));
		return role;
	}
}
