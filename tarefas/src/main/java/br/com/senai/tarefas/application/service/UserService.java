package br.com.senai.tarefas.application.service;


import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.dto.userDTOs.CreateUserDTO;
import br.com.senai.tarefas.application.dto.userDTOs.UpdateUserDTO;
import br.com.senai.tarefas.application.dto.userDTOs.UserResponseDTO;
import br.com.senai.tarefas.application.dto.userDTOs.UserResumoDTO;
import br.com.senai.tarefas.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.senai.tarefas.model.entites.Tarefa;
import br.com.senai.tarefas.model.entites.User;
import br.com.senai.tarefas.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<UserResponseDTO> getUsers(){
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    public Optional<UserResponseDTO> getById(UUID id){
        return Optional.of(repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Id não encontrado")));
    }
    public UUID createUser(CreateUserDTO dto){
        User user = new User();
            user.setEmail(dto.email());
            user.setName(dto.name());

            return repository.save(user).getId();
    }

    @Transactional
    public void deleteUser(UUID id){
        var userId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado"));
        repository.delete(userId);
        repository.flush();
    }

    @Transactional
    public void updateUser(UUID id, UpdateUserDTO dto){
        var pessoaId = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado"));
        pessoaId.setName(dto.name());
        pessoaId.setEmail(dto.email());
    }


    private UserResponseDTO toDto (User user){
        List<TarefaResponseDTO> tarefasDto = user.getTarefa()
                .stream()
                .map(this::toTarefaDto)
                .toList();

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                tarefasDto
        );
    }
    private TarefaResponseDTO toTarefaDto(Tarefa tarefa){
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getDescricao(),
                tarefa.getDataCriacao(),
                tarefa.getStatus(),
                tarefa.getLevel(),
                new UserResumoDTO(
                        tarefa.getUser().getId(),
                        tarefa.getUser().getName(),
                        tarefa.getUser().getEmail()
                )
        );
    }

}
