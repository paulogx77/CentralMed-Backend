package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.GuiaConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuiaConsultaRepository extends JpaRepository<GuiaConsulta, Long> {
    // Pode deixar vazio por enquanto
}