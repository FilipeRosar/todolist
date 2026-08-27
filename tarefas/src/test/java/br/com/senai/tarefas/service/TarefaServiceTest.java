package br.com.senai.tarefas.service;

import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.service.TarefaService;
import br.com.senai.tarefas.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.senai.tarefas.model.entites.Tarefa;
import br.com.senai.tarefas.model.enumered.LevelDifficulty;
import br.com.senai.tarefas.model.enumered.StatusTarefa;
import br.com.senai.tarefas.repository.TarefaRepository;
import br.com.senai.tarefas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {
    @Mock
    private TarefaRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private UUID tarefaId;
    private Tarefa tarefa;
    private UUID userId;

    @BeforeEach
    void setUp(){
        tarefaId = UUID.randomUUID();

        var user = userRepository.findById(userId);

        tarefa = new Tarefa(
                tarefaId,
                "Estudar Java",
                StatusTarefa.PENDENTE,
                LocalDateTime.now(),
                null,
                LevelDifficulty.LOW,
                user.get()
        );
    }
    @Test
    void deveBuscarTarefaPorIdComSucesso(){
        when(repository.findById(tarefaId)).thenReturn(Optional.of(tarefa));

        TarefaResponseDTO responseDTO = tarefaService.findTarefaById(tarefaId);

        assertNotNull(responseDTO);
        assertEquals(tarefaId, responseDTO.id());
        assertEquals("Estudar Java", responseDTO.descricao());
        assertEquals(StatusTarefa.PENDENTE, responseDTO.statusTarefa());

        verify(repository, times(1))
                .findById(tarefaId);
    }
    @Test
    void deveLancarExcecaiQuandoTarefaNaoExistir(){
        when(repository.findById(tarefaId))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> tarefaService.findTarefaById(tarefaId));

        verify(repository, times(1))
                .findById(tarefaId);
    }
}
