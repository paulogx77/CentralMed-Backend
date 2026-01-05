package br.edu.ifpb.CentralMed.controller;


import br.edu.ifpb.CentralMed.dto.PagamentoRequestDTO;
import br.edu.ifpb.CentralMed.model.LancamentoFinanceiro;
import br.edu.ifpb.CentralMed.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {
    @Autowired private FinanceiroService service;

    @PostMapping("/pagar")
    public LancamentoFinanceiro pagar(@RequestBody PagamentoRequestDTO dto) {
        return service.processarPagamentoSimulado(dto);
    }
}
