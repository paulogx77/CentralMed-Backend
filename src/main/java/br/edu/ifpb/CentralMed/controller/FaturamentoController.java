package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.GuiaConsulta;
import br.edu.ifpb.CentralMed.service.FaturamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faturamento")
public class FaturamentoController {

    @Autowired
    private FaturamentoService faturamentoService;

    @GetMapping("/guias-abertas")
    public List<GuiaConsulta> listarGuiasEmAberto() {
        return faturamentoService.buscarGuiasAbertas();
    }

    @PostMapping("/faturar-lote")
    public ResponseEntity<Void> faturarLote(@RequestBody List<Long> guiasIds) {
        faturamentoService.faturarLote(guiasIds);
        return ResponseEntity.ok().build();
    }
}