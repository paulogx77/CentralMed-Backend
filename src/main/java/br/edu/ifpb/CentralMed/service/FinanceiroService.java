package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.PagamentoRequestDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class FinanceiroService {
    @Autowired private LancamentoFinanceiroRepository financeiroRepository;
    @Autowired private ConsultaRepository consultaRepository;

    public LancamentoFinanceiro processarPagamentoSimulado(PagamentoRequestDTO dto) {
        Consulta c = consultaRepository.findById(dto.getConsultaId()).orElseThrow();

        LancamentoFinanceiro lf = new LancamentoFinanceiro();
        lf.setConsulta(c);
        lf.setValor(dto.getValor());
        lf.setFormaPagamento(dto.getFormaPagamento());
        lf.setTipo("RECEITA");
        lf.setDataLancamento(LocalDateTime.now());

        return financeiroRepository.save(lf);
    }
}