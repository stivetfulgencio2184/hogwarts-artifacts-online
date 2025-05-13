package org.alpha.omega.hogwarts_artifacts_online.user.mapper;

import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.response.dto.UserDTO;
import org.alpha.omega.hogwarts_artifacts_online.user.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDto(User user);

    List<UserDTO> toUsersDTOs(List<User> users);

    User toUser(UserRequest request);
}
