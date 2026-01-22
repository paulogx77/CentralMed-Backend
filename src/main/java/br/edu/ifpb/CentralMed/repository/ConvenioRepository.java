package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ConvenioRepository extends JpaRepository<Convenio, Long> {
    Optional<Convenio> findByRegistroAns(String registroAns);
    Optional<Convenio> findByNome(String nome); 
}

