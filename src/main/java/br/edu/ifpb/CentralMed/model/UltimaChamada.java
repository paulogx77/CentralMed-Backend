package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor; // ADICIONE ESTE IMPORT
import lombok.AllArgsConstructor; // Mantenha este

import java.time.LocalDateTime;

@Data
@NoArgsConstructor    // <-- ADICIONE ESTA ANOTAÇÃO
@AllArgsConstructor
@Entity
public class UltimaChamada {
    @Id
    private Long id = 1L;

    private String senha;
    private String local;
    private LocalDateTime dataHora;
}