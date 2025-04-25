package org.alpha.omega.hogwarts_artifacts_online.wizard.repository;

import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, Long> {
}
