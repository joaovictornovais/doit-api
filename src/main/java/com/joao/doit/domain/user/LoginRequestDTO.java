package com.joao.doit.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O e-mail não pode estar vazio")
        @Email(message = "O E-mail deve estar bem formatado. Exemplo: seu_email@exemplo.com")
        String email,
        @NotBlank(message = "A senha não pode estar vazia")
        String password) {
}
