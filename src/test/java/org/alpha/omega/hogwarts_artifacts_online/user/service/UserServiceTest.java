package org.alpha.omega.hogwarts_artifacts_online.user.service;

import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.utility.Utility;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.user.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.users = Utility.produceUsers(7);
    }

    @Test
    void testFindUserById() {
        // Given
        User foundUser = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$SadracFul21$$")
                .build();
        given(this.userRepository.findById(TestConstant.USER_ID)).willReturn(Optional.of(foundUser));

        // When
        User user = this.userService.findUserById(TestConstant.USER_ID);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(foundUser.getId());
        assertThat(user.getEnabled()).isTrue();
        assertThat(user.getUsername()).isEqualTo(foundUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(foundUser.getPassword());
    }

    @Test
    void testFindUserByIdNotFound() {
        // Given
        given(this.userRepository.findById(TestConstant.USER_ID)).willReturn(Optional.empty());

        // When
        assertThrows(NotFoundException.class, () -> this.userService.findUserById(TestConstant.USER_ID));

        // Then
        verify(this.userRepository, times(1)).findById(TestConstant.USER_ID);
    }

    @Test
    void testFindAllUsers() {
        // Given
        given(this.userRepository.findAll()).willReturn(this.users);

        // When
        List<User> userList = this.userService.findAllUsers();

        // Then
        assertThat(userList).isNotNull().isNotEmpty().hasSameSizeAs(this.users);
        assertThat(userList.get(3).getId()).isEqualTo(this.users.get(3).getId());
        assertThat(userList.get(3).getUsername()).isEqualTo(this.users.get(3).getUsername());
        assertThat(userList.get(3).getPassword()).isEqualTo(this.users.get(3).getPassword());
        assertThat(userList.get(3).getEnabled()).isEqualTo(this.users.get(3).getEnabled());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testFindAllUsersEmpty() {
        // Given
        given(this.userRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<User> userList = this.userService.findAllUsers();

        // Then
        assertThat(userList).isNotNull().isEmpty();
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testSaveUserAlreadyRegistered() {
        // Given
        User userToSave = User.builder()
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        User registeredUser  = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        given(this.userRepository.findByUsername(userToSave.getUsername())).willReturn(Optional.of(registeredUser));

        // When
        assertThrows(AlreadyRegisteredException.class, () -> this.userService.saveUser(userToSave));

        // Then
        verify(this.userRepository, times(1)).findByUsername(userToSave.getUsername());
    }

    @Test
    void testSaveUser() {
        // Given
        User userToSave = User.builder()
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        User registeredUser = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .build();
        given(this.userRepository.findByUsername(userToSave.getUsername())).willReturn(Optional.empty());
        given(this.userRepository.save(userToSave)).willReturn(registeredUser);

        // When
        User savedUser = this.userService.saveUser(userToSave);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEnabled()).isEqualTo(userToSave.getEnabled());
        assertThat(savedUser.getUsername()).isEqualTo(userToSave.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(userToSave.getPassword());
        verify(this.userRepository, times(1)).findByUsername(userToSave.getUsername());
        verify(this.userRepository, times(1)).save(userToSave);
    }

    @Test
    void testUpdateUserNotFound() {
        // Given
        User updatedUser = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.FALSE)
                .username("sys")
                .password("$#pass_changed#$")
                .build();
        given(this.userRepository.findById(updatedUser.getId())).willReturn(Optional.empty());

        // When
        assertThrows(NotFoundException.class, () -> this.userService.updateUser(updatedUser));

        // Then
        verify(this.userRepository, times(1)).findById(updatedUser.getId());
    }

    @Test
    void testUpdateUser() {
        // Given
        User updatedUser = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.FALSE)
                .username("user")
                .build();
        User userToUpdate = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.TRUE)
                .username("sys")
                .password("$#SysAdm#$")
                .build();
        given(this.userRepository.findById(TestConstant.USER_ID)).willReturn(Optional.of(userToUpdate));
        given(this.userRepository.save(userToUpdate)).willReturn(userToUpdate);

        // When
        User user = this.userService.updateUser(updatedUser);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(updatedUser.getId());
        assertThat(user.getEnabled()).isEqualTo(updatedUser.getEnabled());
        assertThat(user.getUsername()).isEqualTo(updatedUser.getUsername());
        verify(this.userRepository, times(1)).findById(updatedUser.getId());
        verify(this.userRepository, times(1)).save(userToUpdate);
    }

    @Test
    void testDeleteUserNotFound() {
        // Given
        given(this.userRepository.findById(TestConstant.USER_ID)).willReturn(Optional.empty());

        // When
        assertThrows(NotFoundException.class, () -> this.userService.deleteUser(TestConstant.USER_ID));

        // Then
        verify(this.userRepository, times(1)).findById(TestConstant.USER_ID);
    }

    @Test
    void testDeleteUser() {
        // Given
        User userToDelete = User.builder()
                .id(TestConstant.USER_ID)
                .enabled(Boolean.TRUE)
                .username("sfulgencio")
                .password("$#sys_admin#$")
                .build();
        given(this.userRepository.findById(TestConstant.USER_ID)).willReturn(Optional.of(userToDelete));
        doNothing().when(this.userRepository).delete(userToDelete);

        // When
        this.userService.deleteUser(TestConstant.USER_ID);

        // Then
        verify(this.userRepository, times(1)).findById(TestConstant.USER_ID);
        verify(this.userRepository, times(1)).delete(userToDelete);
    }
}
