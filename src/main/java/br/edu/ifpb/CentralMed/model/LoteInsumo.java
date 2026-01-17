package br.edu.ifpb.CentralMed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class LoteInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroLote;
    private LocalDate dataValidade;
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    @JsonIgnore
    private EstoqueInsumos insumo;

    // Método para checar se o lote está vencido
    public boolean isVencido() {
        if (dataValidade == null) {
            return true;
        }
        return LocalDate.now().isAfter(dataValidade);
    }
}