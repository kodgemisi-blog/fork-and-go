package com.kodgemisi.blog.dynamicauthorization.usermanagement;

import org.springframework.data.repository.CrudRepository;

/**
 * Created on May, 2019
 *
 * @author destan
 */
interface PermissionRepository extends CrudRepository<Permission, String> {

}
