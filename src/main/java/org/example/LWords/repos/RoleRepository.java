package org.example.LWords.repos;

import org.example.LWords.Entities.ERole;
import org.example.LWords.Entities.Role;
import org.example.LWords.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(ERole name);
}
