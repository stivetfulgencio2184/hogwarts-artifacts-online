package org.alpha.omega.hogwarts_artifacts_online.artifact.service;

import org.alpha.omega.hogwarts_artifacts_online.artifact.constant.ConstantTest;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.artifact.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.artifact.utility.IdWorker;
import org.alpha.omega.hogwarts_artifacts_online.artifact.utility.Utility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(value = MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository repository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService service;

    List<Artifact> artifacts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.artifacts = Utility.produceArtifacts(7);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository
        Wizard wizard = Wizard.builder()
                .id(1L)
                .name("")
                .build();
        Artifact artifact = Artifact.builder()
                .id(ConstantTest.ARTIFACT_ID)
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisibility.")
                .imageUrl("")
                .wizard(wizard)
                .build();
        // Defines the behavior of the mock object.
        given(this.repository.findById(ConstantTest.ARTIFACT_ID)).willReturn(Optional.of(artifact));

        // When. Act on the target behavior. When steps should cover the method to be tested.
        Artifact returnedArtifact = this.service.findById(ConstantTest.ARTIFACT_ID);

        // Then. Assert expected outcomes.
        assertNotNull(returnedArtifact);
        assertThat(returnedArtifact).isEqualTo(artifact);
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        verify(this.repository, times(1)).findById(ConstantTest.ARTIFACT_ID);
    }

    @Test
    void testFindByIdNotFound() {
        // Given.
        given(this.repository.findById(Mockito.anyString())).willReturn(Optional.empty());

        // When.
        Throwable thrown = catchThrowable(() -> {
            Artifact returnedArtifact = this.service.findById(ConstantTest.ARTIFACT_ID);
        });

        // Then.
        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(ConstantTest.Exception.Artifact.NOT_FOUNT_ARTIFACT, ConstantTest.ARTIFACT_ID));
        verify(this.repository, times(1)).findById(ConstantTest.ARTIFACT_ID);
    }

    @Test
    void testFindAll() {
        // Given.
        given(this.repository.findAll()).willReturn(this.artifacts);

        // When.
        List<Artifact> artifacts = this.service.findAll();

        // Then.
        assertNotNull(artifacts);
        assertEquals(this.artifacts.size(), artifacts.size());
        assertTrue(Utility.areSameArtifacts(this.artifacts, artifacts));
        verify(this.repository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        // Given.
        given(this.repository.findAll()).willReturn(Collections.emptyList());

        // When.
        List<Artifact> artifacts = this.service.findAll();

        // Then.
        assertNotNull(artifacts);
        assertTrue(artifacts.isEmpty());
        verify(this.repository, times(1)).findAll();
    }

    @Test
    void testSaveArtifact() {
        //Given
        Artifact newArtifact = Artifact.builder()
                .name("Artifact 3")
                .description("Description...!!")
                .imageUrl("imageUrl...!!")
                .build();
        given(this.idWorker.nextId()).willReturn(123456L);
        given(this.repository.save(newArtifact)).willReturn(newArtifact);

        //When
        Artifact savedArtifact = this.service.saveArtifact(newArtifact);

        //Then
        assertNotNull(savedArtifact);
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(this.repository, times(1)).save(newArtifact);
    }
}