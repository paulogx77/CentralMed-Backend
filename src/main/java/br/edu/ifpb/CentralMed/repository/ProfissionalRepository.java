package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    // --- Método para o Spring Security ---
    Optional<UserDetails> findUserDetailsByUsuarioLogin(String usuarioLogin);

    // --- Método para USO INTERNO (Ex: Cadastro, Perfil) ---
    Optional<Profissional> findByUsuarioLogin(String usuarioLogin);

}