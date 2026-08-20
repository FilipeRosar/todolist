package br.com.senai.tarefas.application.service;


import br.com.senai.tarefas.application.dto.tarefaDTOs.CreateTarefaDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.UpdateTarefaDTO;
import br.com.senai.tarefas.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.senai.tarefas.model.entites.Tarefa;
import br.com.senai.tarefas.model.entites.User;
import br.com.senai.tarefas.model.enumered.StatusTarefa;
import br.com.senai.tarefas.repository.TarefaRepository;
import br.com.senai.tarefas.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository repository;

    private final UserRepository userRepository;

    @CacheEvict(value = "tarefas", allEntries = true)
    public UUID cadastrar(CreateTarefaDTO dto){

        User user = userRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Tarefa tarefa = new Tarefa();

        tarefa.setDescricao(dto.descricao());

        tarefa.setDataCriacao(LocalDateTime.now());

        tarefa.setStatus(StatusTarefa.PENDENTE);

        tarefa.setUser(user);

        return repository.save(tarefa).getId();
    }

    @Cacheable(value = "tarefa")
    public Page<TarefaResponseDTO> getAllTarefas(Pageable pageable){
        return repository.findAll(pageable)
                .map(this::toDto);
    }
    @Cacheable(value = "tarefa")
    public TarefaResponseDTO findTarefaById(UUID id){
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
    }

    @Transactional
    @CacheEvict(value = "tarefas", allEntries = true)
    public void updateTarefaDTO(UUID id, UpdateTarefaDTO dto){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));

        tarefaId.setDescricao(dto.descricao());
        tarefaId.setDataUpdate(dto.updated());
    }

    @Transactional
    @CacheEvict(value = "tarefas", allEntries = true)
    public void deleteById(UUID id){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));

        repository.deleteById(tarefaId.getId());
    }
    @Transactional
    @CacheEvict(value = "tarefas", allEntries = true)
    public void finishedTarefa(UUID id){
        var tarefaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
        tarefaId.setStatus(StatusTarefa.CONCLUIDA);
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
