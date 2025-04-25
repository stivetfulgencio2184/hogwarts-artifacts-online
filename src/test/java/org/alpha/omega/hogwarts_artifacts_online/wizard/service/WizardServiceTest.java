package org.alpha.omega.hogwarts_artifacts_online.wizard.service;

import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.repository.WizardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(value = MockitoExtension.class)
class WizardServiceTest {

    @Mock
    private WizardRepository repository;

    @InjectMocks
    private WizardService service;

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
}
