package org.alpha.omega.hogwarts_artifacts_online.wizard.mapper;

import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.response.dto.WizardDTO;
import org.alpha.omega.hogwarts_artifacts_online.wizard.request.WizardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    List<WizardDTO> toWizardsDTOs(List<Wizard> wizards);

    Wizard toWizard(WizardRequest request);
}
