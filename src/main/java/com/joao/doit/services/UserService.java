package com.joao.doit.services;

import com.joao.doit.domain.user.*;
import com.joao.doit.infra.auth.TokenService;
import com.joao.doit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TokenResponseDTO register(RegisterRequestDTO data) {
        if (userRepository.findByEmail(data.email()).isPresent())
            throw new RuntimeException("E-mail já cadastrado");

        User user = new User(data);
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        String token = tokenService.generateToken(user);
        return new TokenResponseDTO(data.firstName(), token);

    }

    public TokenResponseDTO login(LoginRequestDTO data) {
        Optional<User> user = userRepository.findByEmail(data.email());
        if (user.isPresent()) {
            if (passwordEncoder.matches(data.password(), user.get().getPassword())) {
                String token = tokenService.generateToken(user.get());
                return new TokenResponseDTO(user.get().getFirstName(), token);
            }
        }
        throw new RuntimeException("Informações incorretas");
    }

}
