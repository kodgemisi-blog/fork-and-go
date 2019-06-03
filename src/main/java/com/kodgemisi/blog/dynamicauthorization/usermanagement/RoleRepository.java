package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Created on May, 2019
 *
 * @author destan
 */
interface RoleRepository extends PagingAndSortingRepository<Role, String> {

	@Query("SELECT r.permissions FROM Role r WHERE r IN :roles")
	Set<Permission> findPermissionsOfRoles(@Param("roles") Set<Role> roles);

	@Override
	Iterable<Role> findAll(Sort sort);
}
