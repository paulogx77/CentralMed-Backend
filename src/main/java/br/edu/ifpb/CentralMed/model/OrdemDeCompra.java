package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.StatusOrdem;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class OrdemDeCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataEmissao;
    private LocalDate dataEntregaPrevista;
    private BigDecimal valorTotal;
    @Enumerated(EnumType.STRING)
    private StatusOrdem status;

    @ManyToOne @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "ordemDeCompra", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ItemOrdemDeCompra> itens = new ArrayList<>();
}