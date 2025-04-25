package org.alpha.omega.hogwarts_artifacts_online.artifact.mapper;

import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.request.ArtifactRequest;
import org.alpha.omega.hogwarts_artifacts_online.response.dto.ArtifactDTO;
import org.alpha.omega.hogwarts_artifacts_online.wizard.mapper.WizardMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = WizardMapper.class)
public interface ArtifactMapper {

    ArtifactMapper INSTANCE = Mappers.getMapper(ArtifactMapper.class);

    @Mapping(target = "owner", source = "artifact.wizard")
    ArtifactDTO toArtifactDTO(Artifact artifact);

    List<ArtifactDTO> toArtifactsDTOs(List<Artifact> artifacts);

    Artifact toArtifact(ArtifactRequest request);
}
