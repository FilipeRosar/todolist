package br.com.senai.tarefas.application.dto.tarefaDTOs;

import br.com.senai.tarefas.model.enumered.LevelDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTarefaDTO(
        @NotBlank(message = "A descrição é obrigatoria.")
        @Size(min = 3,
                max = 150,
                message = "A descrição deve possuir entre 3 e 150 caracteres")
        String descricao,
        @NotNull(message = "O usuário é obrigatório.")
        UUID userId,
        @NotNull(message = "A dificuldade é obrigatória.")
        LevelDifficulty levelDificuldade
) {
}
