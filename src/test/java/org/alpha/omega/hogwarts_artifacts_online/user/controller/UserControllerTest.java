package org.alpha.omega.hogwarts_artifacts_online.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.user.mapper.UserMapper;
import org.alpha.omega.hogwarts_artifacts_online.user.request.UserRequest;
import org.alpha.omega.hogwarts_artifacts_online.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Value(value = "${api.endpoint.base-url.v1}")
    private String baseUrl;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.users = Utility.produceUsers(7);
    }

    @Test
    void testGetUserById() throws Exception {
        // Given
        User foundUser = User.builder()
                .id(TestConstant.USER_ID)
                .description("User description.")
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$SadracFul21$$")
                .build();
        given(this.userService.findUserById(TestConstant.USER_ID)).willReturn(foundUser);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/{userId}", TestConstant.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(foundUser.getId()))
                .andExpect(jsonPath("$.data.enabled").value(foundUser.getEnabled()));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        // Given
        doThrow(new NotFoundException(
                String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.USER, TestConstant.USER_ID)))
                .when(this.userService).findUserById(TestConstant.USER_ID);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/{userId}", TestConstant.USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(
                        String.format(TestConstant.Exception.NOT_FOUND_OBJECT, TestConstant.USER, TestConstant.USER_ID)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Given
        given(this.userService.findAllUsers()).willReturn(this.users);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all Success."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[3].id").value(this.users.get(3).getId()))
                .andExpect(jsonPath("$.data[3].description").value(this.users.get(3).getDescription()))
                .andExpect(jsonPath("$.data[3].enabled").value(this.users.get(3).getEnabled()))
                .andExpect(jsonPath("$.data[3].username").value(this.users.get(3).getUsername()))
                .andExpect(jsonPath("$.data[3].password").value(this.users.get(3).getPassword()));
    }

    @Test
    void testGetAllUsersEmpty() throws Exception {
        // Given
        given(this.userService.findAllUsers()).willReturn(Collections.emptyList());

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find all Success."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSaveNewUser() throws Exception {
        // Given
        UserRequest request = UserRequest.builder()
                .description("New user")
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        String json = this.mapper.writeValueAsString(request);
        User registeredUser = User.builder()
                .id(TestConstant.USER_ID)
                .description("New user")
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        given(this.userService.saveUser(Mockito.any(User.class))).willReturn(registeredUser);

        // When and Then
        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("New user created."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(registeredUser.getId()))
                .andExpect(jsonPath("$.data.description").value(registeredUser.getDescription()))
                .andExpect(jsonPath("$.data.enabled").value(registeredUser.getEnabled()))
                .andExpect(jsonPath("$.data.username").value(registeredUser.getUsername()))
                .andExpect(jsonPath("$.data.password").value(registeredUser.getPassword()));
    }

    @Test
    void testSaveNewUserBadRequest() throws Exception {
        // Given
        UserRequest request = UserRequest.builder()
                .description("New user")
                .enabled(Boolean.TRUE)
                .password("$$StivetFul2184$$")
                .build();
        String json = this.mapper.writeValueAsString(request);

        // When and Then
        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(TestConstant.Exception.INVALID_ARGUMENTS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSaveNewUserAlreadyRegistered() throws Exception {
        // Given
        UserRequest request = UserRequest.builder()
                .description("New user")
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        String json = this.mapper.writeValueAsString(request);
        doThrow(new AlreadyRegisteredException(String
                .format(TestConstant.Exception.ALREADY_REGISTERED_OBJECT, TestConstant.USER, request.username())))
                .when(this.userService).saveUser(Mockito.any(User.class));

        // When and Then
        this.mockMvc.perform(post(this.baseUrl + "/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.flag").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value(
                        String.format(TestConstant.Exception.ALREADY_REGISTERED_OBJECT, TestConstant.USER, request.username())));
    }
}
