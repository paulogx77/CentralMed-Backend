package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.TriagemDTO;
import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.Triagem;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.PainelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/triagem")
public class TriagemController {

    private final AtendimentoService atendimentoService;
    private final PainelService painelService;

    // Injeção via construtor
    public TriagemController(AtendimentoService atendimentoService, PainelService painelService) {
        this.atendimentoService = atendimentoService;
        this.painelService = painelService;
    }

    @GetMapping("/fila")
    public List<Agendamento> getFilaTriagem() {
        return atendimentoService.listarFila(StatusAgendamento.AGUARDANDO_TRIAGEM);
    }

    @PostMapping("/{agendamentoId}")
    public Triagem salvarTriagem(@PathVariable Long agendamentoId, @RequestBody TriagemDTO dto) {
        return atendimentoService.realizarTriagem(agendamentoId, dto);
    }

    @PostMapping("/chamar/{agendamentoId}")
    public ResponseEntity<Void> chamarParaTriagem(@PathVariable Long agendamentoId) {
        painelService.registrarChamada(agendamentoId, "Triagem 01");
        return ResponseEntity.ok().build();
    }
}