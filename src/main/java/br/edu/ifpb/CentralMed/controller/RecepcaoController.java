package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.AgendamentoFuturoDTO;
import br.edu.ifpb.CentralMed.dto.AgendamentoImediatoDTO;
import br.edu.ifpb.CentralMed.dto.PacienteDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository;
import br.edu.ifpb.CentralMed.repository.PacienteRepository;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.PainelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ifpb.CentralMed.repository.AgendamentoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recepcao")
public class RecepcaoController {

    @Autowired private AtendimentoService service;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private AgendamentoRepository agendamentoRepo;
    @Autowired private PainelService painelService;
    @Autowired private ConvenioRepository convenioRepository;
    @Autowired private PacienteRepository pacienteRepository;

    // --- PACIENTES ---

    @PostMapping("/pacientes")
    public Paciente criarPaciente(@RequestBody PacienteDTO dto) {
        Paciente novoPaciente = new Paciente();
        novoPaciente.setNome(dto.getNome());
        novoPaciente.setCpf(dto.getCpf());
        novoPaciente.setDataNasc(dto.getDataNasc());
        novoPaciente.setAlergiasComorbidades(dto.getAlergiasComorbidades());
        novoPaciente.setEmail(dto.getEmail());

        if (dto.getConvenio() != null && !dto.getConvenio().equalsIgnoreCase("Particular")) {
            Convenio convenio = convenioRepository.findByNome(dto.getConvenio())
                    .orElseThrow(() -> new RuntimeException("Convênio não encontrado: " + dto.getConvenio()));
            novoPaciente.setConvenio(convenio);
        }

        return pacienteRepository.save(novoPaciente);
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

    // CORREÇÃO AQUI: Unifiquei os métodos.
    // O URL é "/agendamentos" (para bater com o front)
    // O parâmetro é "AgendamentoFuturoDTO" (para bater com o service)
    @PostMapping("/agendamentos")
    public Agendamento agendarFuturo(@RequestBody AgendamentoFuturoDTO dto) {
        return service.criarAgendamentoFuturo(dto);
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
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id) {
        service.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    // --- FILAS E PAINEL ---

    @GetMapping("/fila/triagem")
    public List<Agendamento> getFilaTriagem() {
        return service.listarFila(StatusAgendamento.AGUARDANDO_TRIAGEM);
    }

    @PostMapping("/painel/chamar/{id}")
    public ResponseEntity<Void> chamarNoPainel(@PathVariable Long id, @RequestParam(required = false) String local) {
        String localChamada = (local != null && !local.isEmpty()) ? local : "Recepção";
        painelService.registrarChamada(id, localChamada);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/agendamentos/horarios-ocupados")
    public List<LocalTime> getHorariosOcupados(@RequestParam LocalDate data, @RequestParam Long medicoId) {
        return agendamentoRepo.findByDataAndMedicoId(data, medicoId).stream()
                .map(Agendamento::getHora)
                .collect(Collectors.toList());
    }
}