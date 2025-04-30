package org.alpha.omega.hogwarts_artifacts_online.artifact.service;

import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.IdWorker;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
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
import static org.mockito.BDDMockito.willDoNothing;
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
        // In this section go all logic for tear down
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository
        Wizard wizard = Wizard.builder()
                .id(1L)
                .name("")
                .build();
        Artifact artifact = Artifact.builder()
                .id(TestConstant.ARTIFACT_ID)
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisibility.")
                .imageUrl("")
                .wizard(wizard)
                .build();
        // Defines the behavior of the mock object.
        given(this.repository.findById(TestConstant.ARTIFACT_ID)).willReturn(Optional.of(artifact));

        // When. Act on the target behavior. When steps should cover the method to be tested.
        Artifact returnedArtifact = this.service.findById(TestConstant.ARTIFACT_ID);

        // Then. Assert expected outcomes.
        assertNotNull(returnedArtifact);
        assertThat(returnedArtifact).isEqualTo(artifact);
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
    }

    @Test
    void testFindByIdNotFound() {
        // Given.
        given(this.repository.findById(Mockito.anyString())).willReturn(Optional.empty());

        // When.
        Throwable thrown = catchThrowable(() -> {
            this.service.findById(TestConstant.ARTIFACT_ID);
        });

        // Then.
        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT, TestConstant.ARTIFACT_ID));
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
    }

    @Test
    void testFindAll() {
        // Given.
        given(this.repository.findAll()).willReturn(this.artifacts);

        // When.
        List<Artifact> allArtifacts = this.service.findAll();

        // Then.
        assertNotNull(allArtifacts);
        assertEquals(this.artifacts.size(), allArtifacts.size());
        assertTrue(Utility.areSameArtifacts(this.artifacts, allArtifacts));
        verify(this.repository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        // Given.
        given(this.repository.findAll()).willReturn(Collections.emptyList());

        // When.
        List<Artifact> allArtifacts = this.service.findAll();

        // Then.
        assertNotNull(allArtifacts);
        assertTrue(allArtifacts.isEmpty());
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

    @Test
    void testUpdateNotFound() {
        // Given
        Artifact artifactUpdated = Artifact.builder()
                .id(TestConstant.ARTIFACT_ID)
                .name("updated name.")
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();
        given(this.repository.findById(TestConstant.ARTIFACT_ID)).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
           this.service.update(artifactUpdated);
        });

        // Then
        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT, TestConstant.ARTIFACT_ID));
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Artifact artifactUpdated = Artifact.builder()
                .id(TestConstant.ARTIFACT_ID)
                .name("updated name.")
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();

        Artifact artifactToUpdate = Artifact.builder()
                .id(TestConstant.ARTIFACT_ID)
                .name("this is its name.")
                .description("this is its description.")
                .imageUrl("this is its image url.")
                .build();
        given(this.repository.findById(TestConstant.ARTIFACT_ID)).willReturn(Optional.of(artifactToUpdate));
        given(this.repository.save(artifactToUpdate)).willReturn(artifactToUpdate);

        // When
        Artifact artifact = this.service.update(artifactUpdated);

        // Then
        assertNotNull(artifact);
        assertThat(artifact.getName()).isEqualTo(artifactUpdated.getName());
        assertThat(artifact.getDescription()).isEqualTo(artifactUpdated.getDescription());
        assertThat(artifact.getImageUrl()).isEqualTo(artifactUpdated.getImageUrl());
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
        verify(this.repository, times(1)).save(artifactToUpdate);
    }

    @Test
    void testDelete() {
        // Given
        Artifact artifactToDelete = Artifact.builder()
                .id(TestConstant.ARTIFACT_ID)
                .name("Deluminator")
                .description("A deluminator is a device invented by Albus ")
                .imageUrl("first imageUrl")
                .build();
        given(this.repository.findById(TestConstant.ARTIFACT_ID)).willReturn(Optional.of(artifactToDelete));
        willDoNothing().given(this.repository).delete(artifactToDelete);

        // When
        this.service.delete(TestConstant.ARTIFACT_ID);

        // Then
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
        verify(this.repository, times(1)).delete(artifactToDelete);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.repository.findById(TestConstant.ARTIFACT_ID)).willReturn(Optional.empty());

        // When
        assertThrows(NotFoundException.class, () -> this.service.delete(TestConstant.ARTIFACT_ID));

        // Then
        verify(this.repository, times(1)).findById(TestConstant.ARTIFACT_ID);
    }
}