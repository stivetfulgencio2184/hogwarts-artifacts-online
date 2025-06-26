package org.alpha.omega.hogwarts_artifacts_online.artifact.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.artifact.request.ArtifactRequest;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@DisplayName(value = "Integration tests for Artifacts API endpoints.")
@Tag(value = "integration")
@ActiveProfiles(value = "dev")
/* Integration tests when Spring Security is turn on */
/* Integration test, as the name suggest, be in focus on integrate different layers of application.
* We are testing of integrate spring boot application from end to end, there is, from controller, the repository and database.
* Also mean, not mocking is equals, will around integration test against testing data store in each to database  */
public abstract class ArtifactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Value(value = "${api.endpoint.base-url.v1}")
    private String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login")
                .with(httpBasic(TestConstant.Security.USER_ADMIN, TestConstant.Security.USER_ADMIN_PASSWORD)));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = TestConstant.Security.AuthType.BEARER + json.getJSONObject("data").getString("token"); // Don't forget to add "Bearer " as a prefix
    }

    @Test
    @Order(value = 1)
    void testFindArtifactById() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(TestConstant.ARTIFACT_ID))
                .andExpect(jsonPath("$.data.name").value("Deluminator"))
                .andExpect(jsonPath("$.data.description").value("A deluminator is a device invented by Albus "))
                .andExpect(jsonPath("$.data.imageUrl").value("first imageUrl"));
    }

    @Test
    @Order(value = 2)
    void testFindArtifactByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID + "7")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String
                        .format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                                TestConstant.ID, TestConstant.ARTIFACT_ID + "7")))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 3)
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllArtifacts() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    @Order(value = 4)
    @DisplayName(value = "Check add artifact with valid input (POST)")
    void testAddArtifact() throws Exception {
        ArtifactRequest request = ArtifactRequest.builder()
                                .name("Artifact 3")
                                .description("Description...!!")
                                .imageUrl("imageUrl...!!")
                                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(request.name()))
                .andExpect(jsonPath("$.data.description").value(request.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(request.imageUrl()));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data", Matchers.hasSize(7)));
    }

    @Test
    @Order(value = 5)
    void testAddArtifactBadRequest() throws Exception {
        ArtifactRequest request = ArtifactRequest.builder()
                .description("Description...!!")
                .imageUrl("imageUrl...!!")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(value = 6)
    void testUpdateArtifact() throws Exception {
        ArtifactRequest request = ArtifactRequest.builder()
                .name("updated name.")
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", this.token)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(TestConstant.ARTIFACT_ID))
                .andExpect(jsonPath("$.data.name").value(request.name()))
                .andExpect(jsonPath("$.data.description").value(request.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(request.imageUrl()));
    }

    @Test
    @Order(value = 7)
    void testUpdateArtifactBadRequest() throws Exception {
        ArtifactRequest request = ArtifactRequest.builder()
                .description("updated description.")
                .imageUrl("updated image url.")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(value = 8)
    void testDeleteArtifactById() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    @Order(value = 9)
    void testDeleteArtifactByIdNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/{artifactId}", TestConstant.ARTIFACT_ID + "7")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.ARTIFACT,
                        TestConstant.ID, TestConstant.ARTIFACT_ID + "7")));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }
}
