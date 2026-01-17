package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.LancamentoFinanceiro;
import br.edu.ifpb.CentralMed.model.NotaFiscal;
import br.edu.ifpb.CentralMed.model.enums.StatusNfs;
import br.edu.ifpb.CentralMed.repository.LancamentoFinanceiroRepository;
import br.edu.ifpb.CentralMed.repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotaFiscalService {

    @Autowired private NotaFiscalRepository notaFiscalRepository;
    @Autowired private LancamentoFinanceiroRepository lancamentoFinanceiroRepository;

    public NotaFiscal emitirNotaParaParticular(Long lancamentoId) {
        LancamentoFinanceiro lanc = lancamentoFinanceiroRepository.findById(lancamentoId)
                .orElseThrow(() -> new RuntimeException("Lançamento financeiro não encontrado"));

        NotaFiscal nf = new NotaFiscal();
        nf.setLancamentoFinanceiro(lanc);
        nf.setValor(lanc.getValor());
        nf.setStatus(StatusNfs.EMITIDA);
        nf.setDataEmissao(LocalDateTime.now());

        long totalNotas = notaFiscalRepository.count();
        nf.setNumero("NFS-" + (totalNotas + 1));
        nf.setChaveAcesso(UUID.randomUUID().toString());

        return notaFiscalRepository.save(nf);
    }
}