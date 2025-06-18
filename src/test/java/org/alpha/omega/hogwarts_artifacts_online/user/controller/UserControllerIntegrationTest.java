package org.alpha.omega.hogwarts_artifacts_online.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.user.request.UserRequest;
import org.alpha.omega.hogwarts_artifacts_online.user.request.UserRequestUpdt;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
/* The next annotations are optionals */
@DisplayName(value = "Integration tests for User API endpoints.")
@Tag(value = "integration")
class UserControllerIntegrationTest {

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
    void testGetUserById() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/{userId}", TestConstant.USER_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(TestConstant.USER_ID))
                .andExpect(jsonPath("$.data.enabled").value(Boolean.TRUE));
    }

    @Test
    @Order(value = 2)
    void testGetUserByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/{userId}", 111)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(
                        String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.USER,
                                TestConstant.ID, 111)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 3)
    void testGetAllUsers() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all Success."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @Order(value = 4)
    void testSaveNewUser() throws Exception {
        UserRequest request = UserRequest.builder()
                .enabled(Boolean.TRUE)
                .username("ssfulgencio")
                .password("$$SadracFul0121$$")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("New user created."))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.enabled").value(request.enabled()))
                .andExpect(jsonPath("$.data.username").value(request.username()));

        this.mockMvc.perform(get(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all Success."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @Order(value = 5)
    void testSaveNewUserBadRequest() throws Exception {
        UserRequest request = UserRequest.builder()
                .enabled(Boolean.TRUE)
                .password("$$SadracFul0121$$")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(value = 6)
    void testSaveNewUserAlreadyRegistered() throws Exception {
        UserRequest request = UserRequest.builder()
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$SadracFul0121$$")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.ALREADY_REGISTERED_OBJECT, TestConstant.USER, request.username())))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 7)
    void testUpdateUserByIdNotFound() throws Exception {
        UserRequestUpdt request = UserRequestUpdt.builder()
                .enabled(Boolean.FALSE)
                .username("sys")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/users/{userId}", 111)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.token)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String
                        .format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.USER, TestConstant.ID, 111)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 8)
    void testUpdateUserById() throws Exception {
        UserRequestUpdt request = UserRequestUpdt.builder()
                .enabled(Boolean.TRUE)
                .username("sys")
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/users/{userId}", 3)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("User updated successfully."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.enabled").value(request.enabled()))
                .andExpect(jsonPath("$.data.username").value(request.username()));
    }

    @Test
    @Order(value = 9)
    void testUpdateUserByIdBadRequest() throws Exception {
        UserRequestUpdt request = UserRequestUpdt.builder()
                .enabled(Boolean.FALSE)
                .build();
        String json = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(put(this.baseUrl + "/users/{userId}", TestConstant.USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(value = 10)
    void testDeleteUserByIdNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/{userId}", 111)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.USER, TestConstant.ID, 111)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(value = 11)
    void testDeleteUserById() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/{userId}", TestConstant.USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(get(this.baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all Success."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }
}
