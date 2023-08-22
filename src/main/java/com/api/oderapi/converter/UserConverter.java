package com.api.oderapi.converter;

import com.api.oderapi.domain.model.User;
import com.api.oderapi.dto.SignupRequestDTO;
import com.api.oderapi.dto.UserDTO;

// @Component
public class UserConverter extends AbstractConverter<User, UserDTO> {

    @Override
    public UserDTO fromEntity(User entity) {

        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .build();
    }

    @Override
    public User fromDTO(UserDTO dto) {

        if (dto == null) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .build();
    }

    public User signup(SignupRequestDTO request) {

        if (request == null) {
            return null;
        }
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

}
