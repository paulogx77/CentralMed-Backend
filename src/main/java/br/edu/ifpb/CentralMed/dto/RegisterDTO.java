package br.edu.ifpb.CentralMed.dto;

import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;

public record RegisterDTO(
        String nome,
        String usuarioLogin, // O nome no JSON do Postman deve ser 'usuarioLogin'
        String senha,
        String cargo,
        String crmRegistro,
        PerfilUsuario perfil)
{
}