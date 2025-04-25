package org.alpha.omega.hogwarts_artifacts_online.wizard.controller;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.service.WizardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WizardService service;

    @Test
    void testFindWizardById() throws Exception {
        // Given
        Wizard wizardToReturn = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("Fake wizard")
                .build();
        given(this.service.findById(TestConstant.WIZARD_ID)).willReturn(wizardToReturn);

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards/{wizardId}", TestConstant.WIZARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(wizardToReturn.getId()))
                .andExpect(jsonPath("$.data.name").value(wizardToReturn.getName()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(this.service.findById(TestConstant.WIZARD_ID))
                .willThrow(new NotFoundException(
                        String.format(Constant.CustomExMessage.Wizard.NOT_FOUND_WIZARD, TestConstant.WIZARD_ID)));

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards/{wizardId}", TestConstant.WIZARD_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        Constant.CustomExMessage.Wizard.NOT_FOUND_WIZARD, TestConstant.WIZARD_ID)));
    }
}
