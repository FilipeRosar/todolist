package br.com.senai.tarefas.api.controller;

import br.com.senai.tarefas.application.dto.tarefaDTOs.CreateTarefaDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.TarefaResponseDTO;
import br.com.senai.tarefas.application.dto.tarefaDTOs.UpdateTarefaDTO;
import br.com.senai.tarefas.application.service.TarefaService;
import br.com.senai.tarefas.model.enumered.LevelDifficulty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tarefa")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TarefaController {
    private final TarefaService tarefaService;

    @GetMapping("/{tarefaId}")
    public ResponseEntity<TarefaResponseDTO> getTarefasById(@Valid @PathVariable("tarefaId") @RequestBody UUID id){
        var tarefaId = tarefaService.findTarefaById(id);
        return ResponseEntity.ok(tarefaId);
    }
    @PostMapping("/cadastrar")
    public ResponseEntity<UUID> cadastrar(@Valid @RequestBody CreateTarefaDTO dto){
        var tarefaCadastrada = tarefaService.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCadastrada);
    }
    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> getAllTarefas(){
        return ResponseEntity.of(Optional.ofNullable(tarefaService.getAllTarefas()));
    }
    @PutMapping("/{tarefaId}")
    public ResponseEntity<Void> update(@PathVariable("tarefaId") @RequestBody @Valid UUID id,
                                       @Valid @RequestBody UpdateTarefaDTO dto){
        tarefaService.updateTarefa(id,dto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@Valid @RequestBody UUID id){
        tarefaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> concluir(@Valid @PathVariable @RequestBody UUID id){
        tarefaService.finishedTarefa(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/level")
    public ResponseEntity<Void> switchLevel(@Valid @PathVariable @RequestBody UUID id, LevelDifficulty newLevel){
        tarefaService.changeDificultLevel(id, newLevel);
        return ResponseEntity.noContent().build();
    }
}
