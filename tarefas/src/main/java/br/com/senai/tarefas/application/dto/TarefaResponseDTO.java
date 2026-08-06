package br.com.senai.tarefas.application.dto;


import java.time.LocalDateTime;

public record TarefaResponseDTO(
        String descricao,
        LocalDateTime dataCriacao
) {
}
