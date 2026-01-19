package br.edu.ifpb.CentralMed.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PacienteDTO {
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private LocalDate dataNasc;
    private String convenio; // Recebe a STRING do formulário
    private String alergiasComorbidades;
}