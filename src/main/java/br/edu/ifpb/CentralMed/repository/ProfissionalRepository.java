package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<UserDetails> findUserDetailsByUsuarioLogin(String usuarioLogin);
    Optional<Profissional> findByUsuarioLogin(String usuarioLogin);
    // No ProfissionalRepository
    List<Profissional> findByCargo(String cargo);
// ou findByCargo(Cargo cargo) se for Enum
}