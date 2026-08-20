package br.com.senai.tarefas.application.dto.tarefaDTOs;


import br.com.senai.tarefas.model.enumered.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public record TarefaResponseDTO(
        UUID id,
        String descricao,

        @CreationTimestamp
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm",timezone = "America/Sao_Paulo")
        LocalDateTime dataCriacao,

        StatusTarefa statusTarefa
) {
}
