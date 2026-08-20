package br.com.senai.tarefas.service;

import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.service.TarefaService;
import br.com.senai.tarefas.model.entites.Tarefa;
import br.com.senai.tarefas.model.entites.User;
import br.com.senai.tarefas.model.enumered.StatusTarefa;
import br.com.senai.tarefas.repository.TarefaRepository;
import br.com.senai.tarefas.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class TarefaServiceCacheTest {
    @MockitoBean
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaService tarefaService;

    @Test
    void DeveBuscarDoBancoApenasNaPrimeiraChamadaEServirDoCache(){
        User user = new User();
        user.setId(UUID.randomUUID());

        UUID tarefaId = UUID.randomUUID();

        Tarefa tarefaMock = new Tarefa(tarefaId, "Estudar Java", StatusTarefa.PENDENTE, LocalDateTime.now(),null, user);

        Mockito.when(tarefaRepository.findById(tarefaId))
                .thenReturn(Optional.of(tarefaMock));

        TarefaResponseDTO primeiraBusca = tarefaService.findTarefaById(tarefaId);

        TarefaResponseDTO segundaTarefa = tarefaService.findTarefaById(tarefaId);

        Assertions.assertEquals(primeiraBusca, segundaTarefa);

        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(tarefaId);
    }
}
