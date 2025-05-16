package org.alpha.omega.hogwarts_artifacts_online.user.service;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Integer userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, userId)));
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
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, updatedUser.getId())));
    }

    public void deleteUser(Integer userId) {
        User userToDelete = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.NOT_FOUND_OBJECT, Constant.USER, userId)
                ));
        this.userRepository.delete(userToDelete);
    }
}
