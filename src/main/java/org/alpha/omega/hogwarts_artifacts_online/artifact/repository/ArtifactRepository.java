package org.alpha.omega.hogwarts_artifacts_online.artifact.repository;

import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}
