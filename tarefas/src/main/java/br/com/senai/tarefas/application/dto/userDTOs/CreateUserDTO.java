package br.com.senai.tarefas.application.dto.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank(message = "O nome não pode ficar vazio.")
        String name,
        @NotBlank(message = "O email não pode ficar vazio.")
        @Email(message = "Formato de email inválido.")
        String email
) {
}
