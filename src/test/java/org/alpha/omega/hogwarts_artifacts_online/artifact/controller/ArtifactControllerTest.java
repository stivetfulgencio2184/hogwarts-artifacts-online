package org.alpha.omega.hogwarts_artifacts_online.artifact.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.mapper.ArtifactMapper;
import org.alpha.omega.hogwarts_artifacts_online.artifact.request.ArtifactRequest;
import org.alpha.omega.hogwarts_artifacts_online.artifact.service.ArtifactService;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)// Turn off Spring Security. This is controller unit tests
@ActiveProfiles(value = "dev")
public abstract class ArtifactControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ArtifactService service;

    @Value(value = "${api.endpoint.base-url.v1}")
    private String baseUrl;

    @Autowired
    ObjectMapper objectMapper;

    private List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = Utility.produceArtifacts(7);
    }

    @AfterEach
    void tearDown() {
        // In this section go all logic for tear down
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given.
            Artifact foundArtifact = this.artifacts.get(0);
            given(this.service.findById("1")).willReturn(foundArtifact);

        // When and Then
            this.mvc.perform(get(this.baseUrl + "/artifacts/{artifactId}", "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.message").value("Find One Success"))
                    .andExpect(jsonPath("$.data.id").value("1"))
                    .andExpect(jsonPath("$.data.name").value("This is name of the artifact: 1"))
                    .andExpect(jsonPath("$.data.description").value("This is a description of the artifact: 1"))
                    .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl-1"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        // Given.
        given(this.service.findById(Mockito.anyString()))
                .willThrow(new NotFoundException(String
                        .format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                                TestConstant.ID, TestConstant.ARTIFACT_ID)));

        // When and Then
        this.mvc.perform(get(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String
                        .format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                                TestConstant.ID, TestConstant.ARTIFACT_ID)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifacts() throws Exception {
        // Given.
        given(this.service.findAll()).willReturn(this.artifacts);

        // When and Then
        this.mvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1"));
    }

    @Test
    void testAddNewArtifact() throws Exception {
        // Given
        ArtifactRequest request = ArtifactRequest.builder()
                .name("Artifact 3")
                .description("Description...!!")
                .imageUrl("imageUrl...!!")
                .build();
        String json = this.objectMapper.writeValueAsString(request);
        Artifact savedArtifact = Artifact.builder()
                .id("123456")
                .name(request.name())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .build();
        given(this.service.saveArtifact(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        // When and Then
        this.mvc.perform(post(this.baseUrl + "/artifacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(savedArtifact.getId()))
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()))
                .andExpect(jsonPath("$.data.owner").isEmpty());
    }

    @Test
    void testAddNewArtifactBadRequest() throws Exception {
        // Given
        ArtifactRequest request = ArtifactRequest.builder()
                .description("Description...!!")
                .imageUrl("imageUrl...!!")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        // When and Then
        this.mvc.perform(post(this.baseUrl + "/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("name"))
                .andExpect(jsonPath("$.data[1].field").value("name"));
    }

    @Test
    void testUpdateArtifact() throws Exception {
        // Given
        ArtifactRequest request = ArtifactRequest.builder()
                .name("updated name.")
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();
        Artifact artifactUpdated = ArtifactMapper.INSTANCE.toArtifact(request);
        artifactUpdated.setId(TestConstant.ARTIFACT_ID);
        String json = this.objectMapper.writeValueAsString(request);
        given(this.service.update(Mockito.any(Artifact.class))).willReturn(artifactUpdated);

        // When and Then
        this.mvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(artifactUpdated.getId()))
                .andExpect(jsonPath("$.data.name").value(artifactUpdated.getName()))
                .andExpect(jsonPath("$.data.description").value(artifactUpdated.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(artifactUpdated.getImageUrl()))
                .andExpect(jsonPath("$.data.owner").isEmpty());
    }

    @Test
    void testUpdateArtifactBadRequest() throws Exception {
        // Given
        ArtifactRequest request = ArtifactRequest.builder()
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        // When and Then
        this.mvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("name"))
                .andExpect(jsonPath("$.data[1].field").value("name"));
    }

    @Test
    void testDeleteArtifact() throws Exception {
        // Given
        willDoNothing().given(this.service).delete(TestConstant.ARTIFACT_ID);

        // When and Then
        this.mvc.perform(delete(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        // Given
        doThrow(new NotFoundException(String.format(
                TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                TestConstant.ID, TestConstant.ARTIFACT_ID)))
                .when(this.service).delete(TestConstant.ARTIFACT_ID);

        // When and Then
        this.mvc.perform(delete(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                        TestConstant.ID, TestConstant.ARTIFACT_ID)))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}