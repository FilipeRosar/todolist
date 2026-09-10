package br.com.senai.tarefas.model.entites;

import br.com.senai.tarefas.model.enumered.LevelDifficulty;
import br.com.senai.tarefas.model.enumered.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_tarefas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100)
    @NotNull(message = "A descrição é obrigatoria.")
    @Size(min = 3,
            max = 150,
            message = "A descrição deve possuir entre 3 e 150 caracteres")
    private String descricao;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTarefa status = StatusTarefa.PENDENTE;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy",timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy",timezone = "America/Sao_Paulo")
    private LocalDateTime dataUpdate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'LOW'")
    @Builder.Default
    private LevelDifficulty level = LevelDifficulty.LOW;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean done = false;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


}
