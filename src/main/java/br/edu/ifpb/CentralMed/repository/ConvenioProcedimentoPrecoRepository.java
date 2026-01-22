package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.ConvenioProcedimentoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConvenioProcedimentoPrecoRepository extends JpaRepository<ConvenioProcedimentoPreco, Long> {

    // Método futuro (útil para o service): Verificar se já existe preço
    // para uma combinação específica antes de criar uma nova.
    Optional<ConvenioProcedimentoPreco> findByConvenioIdAndProcedimentoId(Long convenioId, Long procedimentoId);

}