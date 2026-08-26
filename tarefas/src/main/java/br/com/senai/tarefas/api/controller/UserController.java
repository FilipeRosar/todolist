package br.com.senai.tarefas.api.controller;

import br.com.senai.tarefas.application.dto.userDTOs.CreateUserDTO;
import br.com.senai.tarefas.application.dto.userDTOs.UpdateUserDTO;
import br.com.senai.tarefas.application.dto.userDTOs.UserResponseDTO;
import br.com.senai.tarefas.application.service.UserService;
import br.com.senai.tarefas.model.entites.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@Valid @PathVariable("id") UUID id){
        var userId = userService.getById(id);
        return ResponseEntity.of(userId);
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @PostMapping("/register")
    public ResponseEntity<UUID> createUser(@RequestBody @Valid CreateUserDTO dto){
        var userId = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody @PathVariable("id") UUID id,@Valid @RequestBody UpdateUserDTO dto){
        userService.updateUser(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Valid @PathVariable("id") @RequestBody UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
