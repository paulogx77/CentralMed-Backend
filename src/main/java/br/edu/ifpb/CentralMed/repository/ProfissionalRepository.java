package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    // Método usado pelo Spring Security (Retorna UserDetails)
    UserDetails findByUsuarioLogin(String usuarioLogin);
}
