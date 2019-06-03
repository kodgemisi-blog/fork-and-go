package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
class Role implements GrantedAuthority {

	public static final Role ADMIN = new Role("ADMIN");

	public static final Role READER = new Role("READER");

	@Id
	private String name;

	// Don't use cascade for permissions because then you need to check if the permissions already exists
	// (no body trying to send new permissions via javascript) every time a new role with permissions is submitted
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Permission> permissions = new HashSet<>();

	Role(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + this.name;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Role role = (Role) o;

		return name != null ? name.equals(role.name) : role.name == null;

	}

	void addPermission(String permission) {
		permissions.add(new Permission(permission));
	}

	void removePermission(String permission) {
		this.permissions.remove(Permission.of(permission));
	}
}
