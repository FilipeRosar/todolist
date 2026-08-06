package br.com.senai.tarefas.api.controller;

import br.com.senai.tarefas.application.dto.CreateTarefaDTO;
import br.com.senai.tarefas.application.dto.TarefaResponseDTO;
import br.com.senai.tarefas.application.dto.UpdateTarefaDTO;
import br.com.senai.tarefas.application.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tarefa")
@RequiredArgsConstructor
public class TarefaController {
    private final TarefaService tarefaService;

    @GetMapping("/{tarefaId}")
    public ResponseEntity<TarefaResponseDTO> getTarefasById(@Valid @PathVariable("tarefaId") @RequestBody UUID id){
        var tarefaId = tarefaService.findTarefaById(id);
        return ResponseEntity.of(tarefaId);
    }
    @PostMapping
    public ResponseEntity<UUID> cadastrar(@Valid @RequestBody CreateTarefaDTO dto){
        var tarefaCadastrada = tarefaService.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCadastrada);
    }
    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> getAllTarefas(){
        return ResponseEntity.ok(tarefaService.getAllTarefas());
    }
    @PutMapping("/{tarefaId}")
    public ResponseEntity<Void> update(@PathVariable("tarefaId") @RequestBody @Valid UUID id, @Valid @RequestBody UpdateTarefaDTO dto){
        tarefaService.updateTarefaDTO(id,dto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable("id") @RequestBody UUID id){
        tarefaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping
    public ResponseEntity<Void> concluir(@PathVariable UUID id){
        tarefaService.finishedTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
