package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

	@Id
	@GeneratedValue
	private Long id;

	private String fullName;

	@NotNull
	@Column(unique = true, nullable = false)
	private String email;

	@NotNull
	@Column(nullable = false)
	private String password;

	@ManyToMany
	private Set<Role> roles = new HashSet<>();

	@Transient
	private Set<GrantedAuthority> permissions = new HashSet<>();

	User(String fullName, String email, String password) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
	}

	void addRole(String roleName) {
		this.roles.add(new Role(roleName));
	}

	boolean removeRole(String roleName) {
		return this.roles.remove(new Role(roleName));
	}

	void addRole(Role role) {
		this.roles.add(role);
	}

	boolean removeRole(Role role) {
		return this.roles.remove(role);
	}

	/**
	 * For this method to work properly Role must have EAGER relation to permissions
	 */
	void updateRolesAndPermissions() {
		this.permissions = this.roles.stream().map(Role::getPermissions).flatMap(Collection::stream).collect(Collectors.toSet());
		permissions.addAll(this.roles);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;

		return email != null ? email.equals(user.email) : user.email == null;

	}

	@Override
	public int hashCode() {
		return email != null ? email.hashCode() : 0;
	}
}
