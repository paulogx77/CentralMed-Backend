package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.TabelaPrecos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TabelaPrecosRepository extends JpaRepository<TabelaPrecos, Long> {

    // Método crucial para buscar o preço durante o faturamento
    Optional<TabelaPrecos> findByConvenioIdAndProcedimentoId(Long convenioId, Long procedimentoId);


    Optional<TabelaPrecos> findByConvenioIdAndProcedimentoCodigoTuss(Long convenioId, String codigoTuss);

}