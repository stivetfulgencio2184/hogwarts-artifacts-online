package org.alpha.omega.hogwarts_artifacts_online.artifact.controller;

import org.alpha.omega.hogwarts_artifacts_online.artifact.constant.ConstantTest;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.mapper.ArtifactMapper;
import org.alpha.omega.hogwarts_artifacts_online.artifact.response.ArtifactDTO;
import org.alpha.omega.hogwarts_artifacts_online.artifact.service.ArtifactService;
import org.alpha.omega.hogwarts_artifacts_online.artifact.utility.Utility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ArtifactService service;

    private List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = Utility.produceArtifacts(7);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given.
            Artifact foundArtifact = this.artifacts.get(0);
            given(this.service.findById("1")).willReturn(foundArtifact);

        // When and Then
            this.mvc.perform(get("/api/v1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value("Find One Success"))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.name").value("This is name of the artifact: 1"))
                    .andExpect(jsonPath("$.data.description").value("This is a description of the artifact: 1"))
                    .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl-1"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        // Given.
        given(this.service.findById(Mockito.anyString()))
                .willThrow(new NotFoundException(String
                        .format(ConstantTest.Exception.Artifact.NOT_FOUNT_ARTIFACT, ConstantTest.ARTIFACT_ID)));

        // When and Then
        this.mvc.perform(get("/api/v1/artifacts/10435344876").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String
                        .format(ConstantTest.Exception.Artifact.NOT_FOUNT_ARTIFACT, ConstantTest.ARTIFACT_ID)))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}