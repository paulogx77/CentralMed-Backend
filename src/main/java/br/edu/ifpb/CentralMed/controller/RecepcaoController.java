package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.AgendamentoImediatoDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.PacienteRepository;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recepcao")
public class RecepcaoController {

    @Autowired private AtendimentoService service;
    @Autowired private PacienteRepository pacienteRepo;


    @PostMapping("/pacientes")
    public Paciente criarPaciente(@RequestBody Paciente p) {
        return pacienteRepo.save(p);
    }

    @GetMapping("/pacientes")
    public List<Paciente> listarPacientes() {
        return pacienteRepo.findAll();
    }

    @PutMapping("/pacientes/{id}")
    public Paciente atualizarPaciente(@PathVariable Long id, @RequestBody Paciente p) {
        return service.atualizarPaciente(id, p);
    }

    // --- GESTÃO DE AGENDAMENTOS ---

    @PostMapping("/agendamentos")
    public Agendamento agendarFuturo(@RequestBody Agendamento a) {
        return service.criarAgendamentoFuturo(a);
    }

    @PostMapping("/agendamentos/{id}/checkin")
    public Agendamento checkIn(@PathVariable Long id) {
        return service.realizarCheckInAgendado(id);
    }

    @PostMapping("/atendimento-imediato")
    public Agendamento atendimentoImediato(@RequestBody AgendamentoImediatoDTO dto) {
        return service.criarAtendimentoImediato(dto);
    }

    @DeleteMapping("/agendamentos/{id}")
    public void cancelarAgendamento(@PathVariable Long id) {
        service.cancelarAgendamento(id);
    }

    // --- FILAS E PAINEL ---

    @GetMapping("/fila/triagem")
    public List<Agendamento> getFilaTriagem() {
        return service.listarFila(StatusAgendamento.AGUARDANDO_TRIAGEM);
    }

    @PostMapping("/painel/chamar/{id}")
    public Agendamento chamarNoPainel(@PathVariable Long id) {
        return service.chamarPacientePainel(id);
    }
}