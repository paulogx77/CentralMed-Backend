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

    // Pacientes
    @PostMapping("/pacientes")
    public Paciente criarPaciente(@RequestBody Paciente p) { return pacienteRepo.save(p); }

    @GetMapping("/pacientes")
    public List<Paciente> listarPacientes() { return pacienteRepo.findAll(); }

    // Fluxo 1: Marcar para depois
    @PostMapping("/agendamentos")
    public Agendamento agendarFuturo(@RequestBody Agendamento a) { return service.criarAgendamentoFuturo(a); }

    // Fluxo 1b: Paciente agendado chegou
    @PostMapping("/agendamentos/{id}/checkin")
    public Agendamento checkIn(@PathVariable Long id) { return service.realizarCheckInAgendado(id); }

    // Fluxo 2: Chegou agora (Emergência/Encaixe) - Já faz checkin auto
    @PostMapping("/atendimento-imediato")
    public Agendamento atendimentoImediato(@RequestBody AgendamentoImediatoDTO dto) {
        return service.criarAtendimentoImediato(dto);
    }
}