package org.alpha.omega.hogwarts_artifacts_online.wizard.service;

import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.repository.WizardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(value = MockitoExtension.class)
class WizardServiceTest {

    @Mock
    private WizardRepository repository;

    @InjectMocks
    private WizardService service;

    private List<Wizard> wizards = new ArrayList<>();

    @BeforeEach
    void setUp() { this.wizards = Utility.produceWizards(3); }

    @Test
    void testFoundById() {
        // Given
        Wizard wizardFound = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("Fake wizard")
                .build();
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.of(wizardFound));

        // when
        Wizard wizardToReturn = this.service.findById(TestConstant.WIZARD_ID);

        // Then
        assertNotNull(wizardToReturn);
        assertThat(wizardToReturn.getId()).isEqualTo(wizardFound.getId());
        assertThat(wizardToReturn.getName()).isEqualTo(wizardFound.getName());
        assertThat(wizardToReturn.getArtifacts()).isNull();
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.empty());

        // When and Then
        assertThrows(NotFoundException.class, () -> this.service.findById(TestConstant.WIZARD_ID));
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
    }

    @Test
    void testFindAll() {
        // Given
        given(this.repository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> wizardsList = this.service.findAll();

        // Then
        assertThat(wizardsList).isNotEmpty()
                .hasSize(this.wizards.size())
                .isEqualTo(this.wizards);
        verify(this.repository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        // Given
        given(this.repository.findAll()).willReturn(Collections.emptyList());

        // When
        List<Wizard> wizardsList = this.service.findAll();

        // Then
        assertThat(wizardsList).isNotNull().isEmpty();
        verify(this.repository, times(1)).findAll();
    }

    @Test
    void testCreateWizard() {
        // Given
        Wizard newWizard = Wizard.builder()
                .name("New Wizard to save.")
                .build();
        Wizard savedWizard = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("New Wizard to save.")
                .build();
        given(this.repository.save(newWizard)).willReturn(savedWizard);

        // When
        Wizard wizard = this.service.createWizard(newWizard);

        // Then
        assertThat(wizard).isNotNull();
        assertThat(wizard.getId()).isEqualTo(savedWizard.getId());
        assertThat(wizard.getName()).isEqualTo(savedWizard.getName());
        verify(this.repository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateWizardNotFound() {
        // Given
        Wizard updatedWizard = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("New name for Wizard.")
                .build();
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.empty());

        // When and Then
        assertThrows(NotFoundException.class, () -> this.service.updateWizard(updatedWizard));
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
    }

    @Test
    void testUpdateWizard() {
        // Given
        Wizard wizardToUpdate = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("Wizard name.")
                .build();
        Wizard updatedWizard = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("New name for Wizard.")
                .build();
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.of(wizardToUpdate));
        given(this.repository.save(wizardToUpdate)).willReturn(wizardToUpdate);

        // When
        Wizard wizard = this.service.updateWizard(updatedWizard);

        // Then
        assertThat(wizard).isNotNull();
        assertThat(wizard.getName()).isEqualTo(updatedWizard.getName());
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
        verify(this.repository, times(1)).save(wizardToUpdate);
    }

    @Test
    void testDeleteWizardNotFound() {
        // Given
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.empty());

        // When
        assertThrows(NotFoundException.class, () -> this.service.deleteWizard(TestConstant.WIZARD_ID));

        // Then
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
    }

    @Test
    void testDeleteWizard() {
        // Given
        Wizard wizardToDelete = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("Wizard name.")
                .build();
        given(this.repository.findById(TestConstant.WIZARD_ID)).willReturn(Optional.of(wizardToDelete));
        doNothing().when(this.repository).delete(wizardToDelete);

        // When
        this.service.deleteWizard(TestConstant.WIZARD_ID);

        // Then
        verify(this.repository, times(1)).findById(TestConstant.WIZARD_ID);
        verify(this.repository, times(1)).delete(wizardToDelete);
    }
}
