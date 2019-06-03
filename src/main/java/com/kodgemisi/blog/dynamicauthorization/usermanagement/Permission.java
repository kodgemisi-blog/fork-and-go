package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
class Permission implements GrantedAuthority {

	public static final Permission EMPTY = new Permission();

	@Id
	private String name;

	Permission(String name) {
		this.name = name;
	}

	static Permission of(String permissionName) {
		return new Permission(permissionName);
	}

	@Override
	public String getAuthority() {
		return name;
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

		Permission that = (Permission) o;

		return name != null ? name.equals(that.name) : that.name == null;

	}
}
