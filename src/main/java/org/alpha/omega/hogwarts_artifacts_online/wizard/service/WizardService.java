package org.alpha.omega.hogwarts_artifacts_online.wizard.service;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.repository.WizardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WizardService {

    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public Wizard findById(Long wizardId) {
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.WIZARD, Constant.ID, wizardId)));
    }

    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard createWizard(Wizard newWizard) {
        return this.wizardRepository.save(newWizard);
    }

    public Wizard updateWizard(Wizard updatedWizard) {
        Long wizardId = updatedWizard.getId();
        return this.wizardRepository.findById(wizardId)
                    .map(wizardToUpdate -> {
                            wizardToUpdate.setName(updatedWizard.getName());
                            return this.wizardRepository.save(wizardToUpdate);
                        }).orElseThrow(() -> new NotFoundException(
                            String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.WIZARD, Constant.ID, wizardId)));
    }

    public void deleteWizard(Long id) {
        Wizard wizardToDelete = this.wizardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.WIZARD, Constant.ID, id)));
        wizardToDelete.disassociateArtifactsRelated();
        this.wizardRepository.delete(wizardToDelete);
    }

    public void assignArtifact(Long wizardId, String artifactId) {
        Artifact artifactToAssign = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.ARTIFACT, Constant.ID, artifactId)));
        
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() ->  new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.WIZARD, Constant.ID, wizardId)));

        if (artifactToAssign.getWizard() != null)
            artifactToAssign.getWizard().removeArtifact(artifactToAssign);

        wizard.addArtifact(artifactToAssign);
        this.artifactRepository.save(artifactToAssign);
        this.wizardRepository.save(wizard);
    }
}
