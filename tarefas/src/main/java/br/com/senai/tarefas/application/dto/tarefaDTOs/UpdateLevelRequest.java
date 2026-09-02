package br.com.senai.tarefas.application.dto.tarefaDTOs;

import br.com.senai.tarefas.model.enumered.LevelDifficulty;

public record UpdateLevelRequest(
        LevelDifficulty level
) {
}
