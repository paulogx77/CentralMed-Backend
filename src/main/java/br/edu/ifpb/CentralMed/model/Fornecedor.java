package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A Razão Social é obrigatória.")
    @Column(nullable = false)
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Column(unique = true, nullable = false)
    private String cnpj;

    private String nomeFantasia;
    private String emailContato;
    private String telefoneContato;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;
}