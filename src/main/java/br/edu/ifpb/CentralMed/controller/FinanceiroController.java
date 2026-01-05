package br.edu.ifpb.CentralMed.controller;


import br.edu.ifpb.CentralMed.dto.PagamentoRequestDTO;
import br.edu.ifpb.CentralMed.model.LancamentoFinanceiro;
import br.edu.ifpb.CentralMed.repository.LancamentoFinanceiroRepository;
import br.edu.ifpb.CentralMed.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importe isso

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {

    @Autowired private FinanceiroService service;
    @Autowired private LancamentoFinanceiroRepository repository; // Injete o repository

    @PostMapping("/pagar")
    public LancamentoFinanceiro pagar(@RequestBody PagamentoRequestDTO dto) {
        return service.processarPagamentoSimulado(dto);
    }

    // --- ADICIONE ESTE MÉTODO ABAIXO ---
    @GetMapping
    public List<LancamentoFinanceiro> listarHistorico() {
        return repository.findAll();
    }
}