package org.alpha.omega.hogwarts_artifacts_online.user.repository;

import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
