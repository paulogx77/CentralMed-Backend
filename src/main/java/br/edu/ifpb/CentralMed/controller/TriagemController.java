package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.TriagemDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/triagem")
public class TriagemController {
    @Autowired private AtendimentoService service;

    @GetMapping("/fila")
    public List<Agendamento> getFilaTriagem() {
        return service.listarFila(StatusAgendamento.AGUARDANDO_TRIAGEM);
    }

    @PostMapping("/{agendamentoId}")
    public Triagem salvarTriagem(@PathVariable Long agendamentoId, @RequestBody TriagemDTO dto) {
        return service.realizarTriagem(agendamentoId, dto);
    }
}