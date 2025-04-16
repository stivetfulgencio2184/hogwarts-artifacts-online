package org.alpha.omega.hogwarts_artifacts_online.artifact.mapper;

import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.response.ArtifactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = WizardMapper.class)
public interface ArtifactMapper {

    ArtifactMapper INSTANCE = Mappers.getMapper(ArtifactMapper.class);

    @Mapping(target = "owner", source = "artifact.wizard")
    ArtifactDTO toArtifactDTO(Artifact artifact);
}
