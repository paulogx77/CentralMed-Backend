package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvenioRepository extends JpaRepository<Convenio, Long> {
    // A interface pode ficar vazia, a magia vem do 'extends'
}