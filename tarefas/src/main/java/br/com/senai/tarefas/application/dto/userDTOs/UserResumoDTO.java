package br.com.senai.tarefas.application.dto.userDTOs;

import java.util.UUID;

public record UserResumoDTO(
        UUID id,
        String name,
        String email
) {
}
