package org.alpha.omega.hogwarts_artifacts_online.role.repository;

import org.alpha.omega.hogwarts_artifacts_online.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
