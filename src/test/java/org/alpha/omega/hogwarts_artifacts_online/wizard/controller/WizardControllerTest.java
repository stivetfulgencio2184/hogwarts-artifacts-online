package org.alpha.omega.hogwarts_artifacts_online.wizard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.request.WizardRequest;
import org.alpha.omega.hogwarts_artifacts_online.wizard.service.WizardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    ObjectMapper mapper;

    List<Wizard> wizards = new ArrayList<>();

    @BeforeEach
    void setUp() { this.wizards = Utility.produceWizards(3); }

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

    @Test
    void testFindAllWizards() throws Exception {
        // Given
        given(this.service.findAll()).willReturn(this.wizards);

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].id").value(TestConstant.WIZARD_ID))
                .andExpect(jsonPath("$.data[0].numberOfArtifacts").value(1));
    }

    @Test
    void testFindAllWizardsEmpty() throws Exception {
        // Given
        given(this.service.findAll()).willReturn(Collections.emptyList());

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testCreateNewWizard() throws Exception {
        // Given
        WizardRequest request = WizardRequest.builder()
                .name("Name of new Wizard.")
                .build();
        String json = this.mapper.writeValueAsString(request);
        Wizard createdWizard = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name("Name of new Wizard.")
                .build();
        given(this.service.createWizard(Mockito.any(Wizard.class))).willReturn(createdWizard);

        // When and Then
        this.mockMvc.perform(post("/api/v1/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("New wizard created successfully."))
                .andExpect(jsonPath("$.data.id").value(createdWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(createdWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testCreateWizardBadRequest() throws Exception {
        // Given
        WizardRequest request = WizardRequest.builder().build();
        String json = this.mapper.writeValueAsString(request);

        // When and Then
        this.mockMvc.perform(post("/api/v1/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testUpdateWizardById() throws Exception {
        // Given
        WizardRequest request = WizardRequest.builder()
                .name("New name for Wizard.")
                .build();
        String json = this.mapper.writeValueAsString(request);
        Wizard updatedWizard = Wizard.builder()
                .id(TestConstant.WIZARD_ID)
                .name(request.name())
                .build();
        given(this.service.updateWizard(updatedWizard)).willReturn(updatedWizard);

        // When and Then
        this.mockMvc.perform(put("/api/v1/wizards/{wizardId}", TestConstant.WIZARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Wizard updated successfully."))
                .andExpect(jsonPath("$.data.id").value(TestConstant.WIZARD_ID))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testUpdateWizardByIdBadRequest() throws Exception {
        // Given
        WizardRequest request = WizardRequest.builder()
                .build();
        String json = this.mapper.writeValueAsString(request);

        // When and Then
        this.mockMvc.perform(put("/api/v1/wizards/{wizardId}", TestConstant.WIZARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }
}
