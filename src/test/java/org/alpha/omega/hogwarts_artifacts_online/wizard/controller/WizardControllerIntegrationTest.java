package org.alpha.omega.hogwarts_artifacts_online.wizard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.wizard.request.WizardRequest;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
/* The next annotations are optionals */
@DisplayName(value = "Integration tests for Wizard API endpoints.")
@Tag(value = "integration")
class WizardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${api.endpoint.base-url.v1}")
    private String baseUrl;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login")
                        .with(httpBasic(TestConstant.Security.USER_ADMIN, TestConstant.Security.USER_ADMIN_PASSWORD)));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = TestConstant.Security.AuthType.BEARER + json.getJSONObject("data").getString("token");
    }

    @Test
    @Order(value = 1)
    void testFindWizardById() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/wizards/{wizardId}", TestConstant.WIZARD_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(TestConstant.WIZARD_ID))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
    }

    @Test
    @Order(value = 2)
    void testFindWizardByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/wizards/{wizardId}", 110)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.WIZARD, TestConstant.ID, 110)));
    }

    @Test
    @Order(value = 3)
    void testFindAllWizards() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/wizards")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @Order(value = 4)
    void testCreateWizard() throws Exception {
        WizardRequest request = WizardRequest.builder()
                .name("Name of new Wizard.")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/wizards")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("New wizard created successfully."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        this.mockMvc.perform(get(this.baseUrl + "/wizards")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @Order(value = 5)
    void testCreateWizardBadRequest() throws Exception {
        WizardRequest request = WizardRequest.builder().build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/wizards")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @Order(value = 6)
    void testUpdateWizardById() throws Exception {
        WizardRequest request = WizardRequest.builder()
                .name("New name for Wizard.")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/wizards/{wizardId}", TestConstant.WIZARD_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Wizard updated successfully."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(TestConstant.WIZARD_ID))
                .andExpect(jsonPath("$.data.name").value(request.name()));
    }

    @Test
    @Order(value = 7)
    void testUpdateWizardIdBadRequest() throws Exception {
        WizardRequest request = WizardRequest.builder().build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/wizards/{wizardId}", TestConstant.WIZARD_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @Order(value = 8)
    void testDeleteWizardById() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/{wizardId}", TestConstant.WIZARD_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Wizard delete successfully."))
                .andExpect(jsonPath("$.data").isEmpty());

        this.mockMvc.perform(get(this.baseUrl + "/wizards")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @Order(value = 9)
    void testDeleteWizardByIdNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/{wizardId}", 111L)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(
                        String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.WIZARD,
                                TestConstant.ID, 111L)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 10)
    void testAssignArtifactToWizard() throws Exception {
        this.mockMvc.perform(patch(this.baseUrl + "/wizards/{wizardId}/artifacts/{artifactId}", 3L, TestConstant.ARTIFACT_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 11)
    void testAssignArtifactToWizardNotFoundArtifact() throws Exception {
        this.mockMvc.perform(patch(this.baseUrl + "/wizards/{wizardId}/artifacts/{artifactId}",
                        TestConstant.WIZARD_ID, TestConstant.ARTIFACT_ID + "7")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                        TestConstant.ID, TestConstant.ARTIFACT_ID + "7")))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 12)
    void testAssignArtifactToWizardNotFoundWizard() throws Exception {
        this.mockMvc.perform(patch(this.baseUrl + "/wizards/{wizardId}/artifacts/{artifactId}",
                        111L, TestConstant.ARTIFACT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.WIZARD, TestConstant.ID, 111L)))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
