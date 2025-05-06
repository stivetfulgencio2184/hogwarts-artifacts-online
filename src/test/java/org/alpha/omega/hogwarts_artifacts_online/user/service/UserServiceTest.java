package org.alpha.omega.hogwarts_artifacts_online.user.service;

import org.alpha.omega.hogwarts_artifacts_online.common.constant.TestConstant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(value = MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        //
    }

    @Test
    void testFindUserById() {
        // Given
        User foundUser = User.builder()
                .id(TestConstant.USER_ID)
                .description("User description.")
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
        assertThat(user.getDescription()).isEqualTo(foundUser.getDescription());
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
}
