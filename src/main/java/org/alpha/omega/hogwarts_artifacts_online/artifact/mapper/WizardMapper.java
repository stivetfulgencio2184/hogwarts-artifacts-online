package org.alpha.omega.hogwarts_artifacts_online.artifact.mapper;

import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.artifact.response.WizardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WizardMapper {

    WizardMapper INSTANCE = Mappers.getMapper(WizardMapper.class);

    default WizardDTO toWizardDTO(Wizard wizard) {
        if ( wizard == null ) {
            return null;
        }
        return WizardDTO.builder()
                .id(wizard.getId())
                .name(wizard.getName())
                .numberOfArtifacts(wizard.getNumberOfArtifacts())
                .build();
    }
}
