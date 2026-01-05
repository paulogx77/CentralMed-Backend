package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {
    @Autowired private AtendimentoService atendimentoService;
    @Autowired private MedicoService medicoService;

    @GetMapping("/fila")
    public List<Agendamento> getFilaConsulta() {
        return atendimentoService.listarFila(StatusAgendamento.AGUARDANDO_CONSULTA);
    }

    @GetMapping("/dados-triagem/{agendamentoId}")
    public Triagem getTriagem(@PathVariable Long agendamentoId) {
        return medicoService.buscarDadosTriagem(agendamentoId);
    }

    @PostMapping("/consulta/{agendamentoId}/iniciar")
    public Consulta iniciar(@PathVariable Long agendamentoId) {
        return medicoService.iniciarConsulta(agendamentoId);
    }

    @PostMapping("/consulta/{consultaId}/finalizar")
    public Consulta finalizar(@PathVariable Long consultaId, @RequestBody FinalizarConsultaDTO dto) {
        return medicoService.finalizarConsulta(consultaId, dto);
    }

    @GetMapping("/historico/{pacienteId}")
    public List<Consulta> getHistoricoPaciente(@PathVariable Long pacienteId) {
        return medicoService.buscarHistoricoPaciente(pacienteId);
    }

    @GetMapping("/atestado/{consultaId}")
    public String gerarAtestado(@PathVariable Long consultaId, @RequestParam Integer dias) {
        return medicoService.gerarTextoAtestado(consultaId, dias);
    }
}