package br.com.senai.tarefas.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErroResponse {
    private LocalDateTime dataHora;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
