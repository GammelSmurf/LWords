package org.example.authentication.repos;

import org.example.authentication.Entities.ERole;
import org.example.authentication.Entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(ERole name);
}
