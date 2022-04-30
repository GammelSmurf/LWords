package org.example.authenticationmanager.repos;

import org.example.authenticationmanager.Entities.ERole;
import org.example.authenticationmanager.Entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(ERole name);
}
