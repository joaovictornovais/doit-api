package com.joao.doit.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank(message = "O primeiro nome não pode estar vazio")
        String firstName,
        @NotBlank(message = "O sobrenome não pode estar vazio")
        String lastName,
        @NotBlank(message = "O e-mail não pode estar vazio")
        @Email(message = "O E-mail deve estar bem formatado. Exemplo: seu_email@exemplo.com")
        String email,
        @NotBlank(message = "O primeiro nome não pode estar vazio")
        String password) {
}
