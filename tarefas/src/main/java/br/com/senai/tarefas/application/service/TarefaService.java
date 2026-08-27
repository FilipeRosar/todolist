package br.com.senai.tarefas.application.service;


import br.com.senai.tarefas.application.dto.tarefaDTOs.CreateTarefaDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.UpdateTarefaDTO;
import br.com.senai.tarefas.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.senai.tarefas.model.entites.Tarefa;
import br.com.senai.tarefas.model.enumered.LevelDifficulty;
import br.com.senai.tarefas.model.enumered.StatusTarefa;
import br.com.senai.tarefas.repository.TarefaRepository;
import br.com.senai.tarefas.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository repository;
    private final UserRepository userRepository;

    public UUID cadastrar(CreateTarefaDTO dto){
        Tarefa tarefa = new Tarefa();

        tarefa.setUser(userRepository.findById(dto.userId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado")));

        tarefa.setDescricao(dto.descricao());

        tarefa.setDataCriacao(LocalDateTime.now());

        tarefa.setStatus(StatusTarefa.PENDENTE);

        return repository.save(tarefa).getId();
    }

    public List<TarefaResponseDTO> getAllTarefas(){
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    public TarefaResponseDTO findTarefaById(UUID id){
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
    }

    @Transactional
    public void updateTarefa(UUID id, UpdateTarefaDTO dto){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));

        tarefaId.setDescricao(dto.descricao());
        tarefaId.setDataUpdate(dto.updated());
    }

    @Transactional
    public void deleteById(UUID id){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));

        repository.deleteById(tarefaId.getId());
        repository.save(tarefaId);

    }
    @Transactional
    public void finishedTarefa(UUID id){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
        tarefaId.setStatus(StatusTarefa.CONCLUIDA);
        repository.save(tarefaId);
    }

    @Transactional
    public void changeDificultLevel(UUID id, LevelDifficulty novoNivel){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
        tarefaId.setLevel(novoNivel);

        repository.save(tarefaId);

    }

    private TarefaResponseDTO toDto(Tarefa tarefa){
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getDescricao(),
                tarefa.getDataCriacao(),
                tarefa.getStatus()
        );
    }

}
