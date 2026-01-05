package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Profissional {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cargo;
    private String crmRegistro; // CRM ou COREN
    @Column(unique = true) private String usuarioLogin;
    private String senha;
    @Enumerated(EnumType.STRING) private PerfilUsuario perfil;
}
