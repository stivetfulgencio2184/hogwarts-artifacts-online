package org.alpha.omega.hogwarts_artifacts_online.artifact.service;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.artifact.constant.Constant;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.artifact.utility.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactRepository repository;

    private final IdWorker idWorker;

    public Artifact findById(String id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.Artifact.NOT_FOUNT_ARTIFACT, id)));
    }

    public List<Artifact> findAll() {
        return this.repository.findAll();
    }

    public Artifact saveArtifact(Artifact newArtifact) {
        newArtifact.setId(String.valueOf(this.idWorker.nextId()));
        return this.repository.save(newArtifact);
    }

    public Artifact update(Artifact artifactUpdated) {
        String artifactId = artifactUpdated.getId();
        return this.repository.findById(artifactId)
                .map(artifactToUpdate -> {
                    artifactToUpdate.setName(artifactUpdated.getName());
                    artifactToUpdate.setDescription(artifactUpdated.getDescription());
                    artifactToUpdate.setImageUrl(artifactUpdated.getImageUrl());
                    return this.repository.save(artifactToUpdate);
                }).orElseThrow(() ->
                    new NotFoundException(
                            String.format(Constant.CustomExMessage.Artifact.NOT_FOUNT_ARTIFACT, artifactId)));
    }
}
