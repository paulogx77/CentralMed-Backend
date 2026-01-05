package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByUsuarioLogin(String usuarioLogin);
}
