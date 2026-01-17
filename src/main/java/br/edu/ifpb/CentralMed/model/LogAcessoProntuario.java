package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class LogAcessoProntuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pacienteId;
    private String usuarioLogin; // Quem acessou
    private LocalDateTime dataHoraAcesso;
    private String endpointAcessado; // Ex: /api/medico/historico/1
    private String ipOrigem;
}