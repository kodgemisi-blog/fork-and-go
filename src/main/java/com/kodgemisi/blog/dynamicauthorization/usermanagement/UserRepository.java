package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created on May, 2019
 *
 * @author destan
 */
interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@EntityGraph(attributePaths = { "roles" })
	Optional<User> findByEmail(String email);

}
