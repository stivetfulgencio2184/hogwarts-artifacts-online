package org.alpha.omega.hogwarts_artifacts_online.user.service;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.configuration.security.MyUserPrincipal;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User findUserById(Integer userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, Constant.ID, userId)));
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public User saveUser(User userToSave) {
        User registeredUser = this.userRepository.findByUsername(userToSave.getUsername()).orElse(null);

        if(registeredUser != null)
            throw new AlreadyRegisteredException(
                    String.format(Constant
                            .CustomExMessage.ALREADY_REGISTERED_OBJECT, Constant.USER, userToSave.getUsername()));
        userToSave.setPassword(this.passwordEncoder.encode(userToSave.getPassword()));
        return this.userRepository.save(userToSave);
    }

    public User updateUser(User updatedUser) {
        return this.userRepository.findById(updatedUser.getId())
                .map(userToUpdate -> {
                    userToUpdate.setEnabled(updatedUser.getEnabled());
                    userToUpdate.setUsername(updatedUser.getUsername());
                    return this.userRepository.save(userToUpdate);
                })
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, Constant.ID, updatedUser.getId())));
    }

    public void deleteUser(Integer userId) {
        User userToDelete = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, Constant.ID, userId)
                ));
        this.userRepository.delete(userToDelete);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(MyUserPrincipal::new)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format(
                            Constant.CustomExMessage.USERNAME_NOT_FOUND, username)));
    }
}
