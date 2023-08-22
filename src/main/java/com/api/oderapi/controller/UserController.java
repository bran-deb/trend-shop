package com.api.oderapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.oderapi.converter.UserConverter;
import com.api.oderapi.domain.model.User;
import com.api.oderapi.dto.LoginRequestDTO;
import com.api.oderapi.dto.LoginResponseDTO;
import com.api.oderapi.dto.SignupRequestDTO;
import com.api.oderapi.dto.UserDTO;
import com.api.oderapi.service.UserService;
import com.api.oderapi.utilities.WrapperResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @PostMapping("/signup")
    public ResponseEntity<WrapperResponse<UserDTO>> signup(@RequestBody SignupRequestDTO request) {
        User user = userService.createUser(userConverter.signup(request));
        UserDTO userDTO = userConverter.fromEntity(user);
        WrapperResponse<UserDTO> response = new WrapperResponse<UserDTO>(true, "successs", userDTO);

        return response.createResponse();
    }

    @PostMapping("/login")
    public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO loginResponse = userService.login(request);
        WrapperResponse<LoginResponseDTO> response = new WrapperResponse<LoginResponseDTO>(true, "success",
                loginResponse);

        return response.createResponse();
    }
}
