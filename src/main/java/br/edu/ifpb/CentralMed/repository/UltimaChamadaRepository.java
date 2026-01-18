package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.UltimaChamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimaChamadaRepository extends JpaRepository<UltimaChamada, Long> {}