package br.edu.ifpb.CentralMed.dto;

import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;

public record RegisterDTO(
        String nome,
        String usuarioLogin,
        String senha,
        String cargo,
        String crmRegistro, // Pode ser nulo se for recepcionista
        PerfilUsuario perfil
) {
}