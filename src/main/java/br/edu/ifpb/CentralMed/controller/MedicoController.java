package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.config.auditoria.Auditable;
import br.edu.ifpb.CentralMed.dto.FilaMedicoDTO;
import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.service.MedicoService;
import br.edu.ifpb.CentralMed.service.PainelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    private final MedicoService medicoService;
    private final PainelService painelService;

    public MedicoController(MedicoService medicoService, PainelService painelService) {
        this.medicoService = medicoService;
        this.painelService = painelService;
    }


    @GetMapping("/fila-completa")
    public FilaMedicoDTO getFilaCompleta(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof Profissional)) {
            throw new IllegalStateException("O principal da autenticação não é um Profissional.");
        }
        Profissional profissionalLogado = (Profissional) authentication.getPrincipal();

        List<Agendamento> minhaFila;

        if (profissionalLogado.getPerfil() == PerfilUsuario.ADMIN) {
            minhaFila = medicoService.listarTodasAsFilasDirecionadas();
        } else {
            minhaFila = medicoService.listarFilaDoMedico(profissionalLogado.getId());
        }

        List<Agendamento> filaGeral = medicoService.listarFilaGeral();

        return new FilaMedicoDTO(minhaFila, filaGeral);
    }

    @PostMapping("/atender-fila-geral/{agendamentoId}")
    public Agendamento atenderDaFilaGeral(@PathVariable Long agendamentoId, Authentication authentication) {
        Profissional medico = (Profissional) authentication.getPrincipal();
        return medicoService.atribuirPacienteDaFilaGeral(agendamentoId, medico);
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
    @Auditable // <- Anotação de auditoria está correta aqui
    public List<Consulta> getHistorico(@PathVariable Long pacienteId) {
        return medicoService.getHistoricoPaciente(pacienteId);
    }

    @PostMapping("/atestado/{consultaId}")
    public String gerarAtestado(@PathVariable Long consultaId, @RequestParam Integer dias) {
        return medicoService.gerarTextoAtestado(consultaId, dias);
    }


    @PostMapping("/chamar/{agendamentoId}")
    public ResponseEntity<Void> chamarParaConsulta(@PathVariable Long agendamentoId, Authentication auth) {
        Profissional medico = (Profissional) auth.getPrincipal();
        painelService.registrarChamada(agendamentoId, "Consultório " + medico.getNome());
        return ResponseEntity.ok().build();
    }
}