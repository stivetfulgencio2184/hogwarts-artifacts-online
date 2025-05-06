package org.alpha.omega.hogwarts_artifacts_online.user.repository;

import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
