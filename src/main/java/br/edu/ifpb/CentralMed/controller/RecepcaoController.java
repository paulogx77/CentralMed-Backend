package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.AgendamentoImediatoDTO;
import br.edu.ifpb.CentralMed.dto.PacienteDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository;
import br.edu.ifpb.CentralMed.repository.PacienteRepository;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.PainelService; // <--- IMPORTANTE
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recepcao")
public class RecepcaoController {

    @Autowired private AtendimentoService service;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private PainelService painelService; // <--- INJEÇÃO DO SERVIÇO
    @Autowired private ConvenioRepository convenioRepository; // Importe e injete
    @Autowired private PacienteRepository pacienteRepository;

    @PostMapping("/pacientes")
    public Paciente criarPaciente(@RequestBody PacienteDTO dto) {

        Paciente novoPaciente = new Paciente();
        novoPaciente.setNome(dto.getNome());
        novoPaciente.setCpf(dto.getCpf());
        novoPaciente.setDataNasc(dto.getDataNasc());
        novoPaciente.setAlergiasComorbidades(dto.getAlergiasComorbidades());

        // --- GARANTA QUE ESTA LINHA EXISTA ---
        novoPaciente.setEmail(dto.getEmail());
        // -------------------------------------

        // Lógica do Convênio
        if (dto.getConvenio() != null && !dto.getConvenio().equalsIgnoreCase("Particular")) {
            Convenio convenio = convenioRepository.findByNome(dto.getConvenio())
                    .orElseThrow(() -> new RuntimeException("Convênio não encontrado: " + dto.getConvenio()));
            novoPaciente.setConvenio(convenio);
        } // Se for particular, o campo 'convenio' do paciente já é null por padrão

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
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id) {
        service.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    // --- FILAS E PAINEL ---

    @GetMapping("/fila/triagem")
    public List<Agendamento> getFilaTriagem() {
        return service.listarFila(StatusAgendamento.AGUARDANDO_TRIAGEM);
    }

    // --- O MÉTODO QUE FALTAVA ---
    // Esse é o endpoint que o seu React está chamando: /api/recepcao/painel/chamar/{id}
    @PostMapping("/painel/chamar/{id}")
    public ResponseEntity<Void> chamarNoPainel(@PathVariable Long id, @RequestParam(required = false) String local) {
        // Se vier o local na URL (Front manda ?local=...), usa ele. Se não, usa "Recepção".
        String localChamada = (local != null && !local.isEmpty()) ? local : "Recepção";

        painelService.registrarChamada(id, localChamada);

        return ResponseEntity.ok().build();
    }
}