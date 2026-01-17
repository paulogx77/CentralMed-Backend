package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueInsumos, Long> {

}