package br.com.senai.tarefas.application.dto.userDTOs;

import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.model.entites.Tarefa;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        List<TarefaResponseDTO> tarefa
) {
}
