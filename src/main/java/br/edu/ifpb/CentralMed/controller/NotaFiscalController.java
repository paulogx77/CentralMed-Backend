package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.LancamentoFinanceiro;
import br.edu.ifpb.CentralMed.model.NotaFiscal;
import br.edu.ifpb.CentralMed.repository.LancamentoFinanceiroRepository;
import br.edu.ifpb.CentralMed.repository.NotaFiscalRepository;
import br.edu.ifpb.CentralMed.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notas-fiscais")
public class NotaFiscalController {

    @Autowired private NotaFiscalService notaFiscalService;
    @Autowired private NotaFiscalRepository notaFiscalRepository;
    @Autowired private LancamentoFinanceiroRepository lancamentoFinanceiroRepository;

    /**
     * Lista todas as notas já emitidas (para o Histórico).
     */
    @GetMapping
    public List<NotaFiscal> listarNotas() {
        return notaFiscalRepository.findAll();
    }

    /**
     * Lista pagamentos particulares que ainda não têm nota fiscal.
     */
    @GetMapping("/pendentes/particular")
    public List<LancamentoFinanceiro> listarPendentes() {
        return lancamentoFinanceiroRepository.findLancamentosSemNotaFiscal();
    }

    /**
     * Endpoint para emitir a nota fiscal para um pagamento particular.
     */
    @PostMapping("/emitir/particular/{lancamentoId}")
    public ResponseEntity<NotaFiscal> emitirNota(@PathVariable Long lancamentoId) {
        NotaFiscal nf = notaFiscalService.emitirNotaParaParticular(lancamentoId);
        return ResponseEntity.ok(nf);
    }
}