package br.com.senai.tarefas.application.dto.tarefaDTOs;

import java.time.LocalDateTime;

public record UpdateTarefaDTO(
        String descricao,
        LocalDateTime updated
) {
}
