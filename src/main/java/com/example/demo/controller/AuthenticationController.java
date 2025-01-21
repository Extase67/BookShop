package com.example.demo.controller;

import com.example.demo.dto.user.UserLoginRequestDto;
import com.example.demo.dto.user.UserLoginResponseDto;
import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.security.AuthenticationService;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login to the system")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @Operation(summary = "Registration", description = "Register a new user")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
