package org.alpha.omega.hogwarts_artifacts_online.artifact.controller;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.mapper.ArtifactMapper;
import org.alpha.omega.hogwarts_artifacts_online.artifact.response.Result;
import org.alpha.omega.hogwarts_artifacts_online.artifact.service.ArtifactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService service;

    @GetMapping(path = "/artifacts/{artifactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> findArtifactById(@PathVariable(name = "artifactId") String id) {
        Artifact foundArtifact = this.service.findById(id);
        return ResponseEntity.ok(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("Find One Success")
                        .data(ArtifactMapper.INSTANCE.toArtifactDTO(foundArtifact))
                .build());
    }

    @GetMapping(path = "/artifacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> findAllArtifacts() {
        List<Artifact> artifacts = this.service.findAll();
        return ResponseEntity.ok(Result.builder()
                .flag(Boolean.TRUE)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(ArtifactMapper.INSTANCE.toArtifactsDTOs(artifacts))
                .build());
    }
}
