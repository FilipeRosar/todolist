package br.com.senai.tarefas.application.dto.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email
) {
}
