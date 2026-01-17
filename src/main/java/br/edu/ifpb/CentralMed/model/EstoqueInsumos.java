package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class EstoqueInsumos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer qtdeMinima;

    // Relacionamento com os lotes
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LoteInsumo> lotes = new ArrayList<>();

    public Integer getQtdeAtual() {
        if (lotes == null || lotes.isEmpty()) {
            return 0;
        }
        // Soma a quantidade de todos os lotes deste produto
        return lotes.stream()
                .mapToInt(LoteInsumo::getQuantidade)
                .sum();
    }
}