package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.TabelaPrecos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.Optional;

public interface TabelaPrecosRepository extends JpaRepository<TabelaPrecos, Long> {

    // Busca um preço específico para um convênio e procedimento
    Optional<TabelaPrecos> findByConvenioIdAndProcedimentoId(Long convenioId, Long procedimentoId);
}