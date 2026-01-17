package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Anexo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeOriginal; // Nome que o usuário enviou
    private String nomeArquivo; // Nome seguro salvo no servidor (com UUID)
    private String tipo; // Ex: image/jpeg, application/pdf
    private long tamanho;

    private LocalDateTime dataUpload;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}