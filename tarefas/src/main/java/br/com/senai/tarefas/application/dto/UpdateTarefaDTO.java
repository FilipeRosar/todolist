package br.com.senai.tarefas.application.dto;

import java.time.LocalDateTime;

public record UpdateTarefaDTO(
        String descricao,
        LocalDateTime updated
) {
}
