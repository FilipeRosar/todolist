package br.com.senai.tarefas.model.entites;

import br.com.senai.tarefas.model.enumered.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "tb_tarefas")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tarefa
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 150)
    @NotBlank(message = "A descrição é obrigatoria.")
    @Size(min = 3,
            max = 150,
            message = "A descrição deve possuir entre 3 e 150 caracteres")
    private String descricao;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTarefa status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

}
