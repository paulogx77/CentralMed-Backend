package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.LancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, Long> {


    @Query("SELECT lf FROM LancamentoFinanceiro lf WHERE NOT EXISTS " +
            "(SELECT nf FROM NotaFiscal nf WHERE nf.lancamentoFinanceiro = lf)")
    List<LancamentoFinanceiro> findLancamentosSemNotaFiscal();

}