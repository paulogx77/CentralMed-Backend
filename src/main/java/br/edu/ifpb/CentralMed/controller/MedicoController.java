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
    @Autowired private MedicoService medicoService; // Nome correto

    @GetMapping("/fila")
    public List<Agendamento> getFilaConsulta() {
        return atendimentoService.listarFila(StatusAgendamento.AGUARDANDO_CONSULTA);
    }

    @GetMapping("/dados-triagem/{agendamentoId}")
    public Triagem getTriagem(@PathVariable Long agendamentoId) {
        return medicoService.buscarDadosTriagem(agendamentoId); // Método correto
    }

    @PostMapping("/consulta/{agendamentoId}/iniciar")
    public Consulta iniciar(@PathVariable Long agendamentoId) {
        return medicoService.iniciarConsulta(agendamentoId); // Método correto
    }

    @PostMapping("/consulta/{consultaId}/finalizar")
    public Consulta finalizar(@PathVariable Long consultaId, @RequestBody FinalizarConsultaDTO dto) {
        return medicoService.finalizarConsulta(consultaId, dto); // Método correto
    }

    // Adicione os controllers para os métodos que faltavam
    @GetMapping("/historico/{pacienteId}")
    public List<Consulta> getHistorico(@PathVariable Long pacienteId){
        return medicoService.getHistoricoPaciente(pacienteId);
    }

    @PostMapping("/atestado/{consultaId}")
    public String gerarAtestado(@PathVariable Long consultaId, @RequestParam Integer dias){
        return medicoService.gerarTextoAtestado(consultaId, dias);
    }
}