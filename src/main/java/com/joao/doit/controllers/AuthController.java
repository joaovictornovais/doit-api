package com.joao.doit.controllers;

import com.joao.doit.domain.user.LoginRequestDTO;
import com.joao.doit.domain.user.RegisterRequestDTO;
import com.joao.doit.domain.user.TokenResponseDTO;
import com.joao.doit.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(data));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(data));
    }

}
