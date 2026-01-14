package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.MedicoService;
// --- IMPORT CORRETO ---
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    @Autowired private AtendimentoService atendimentoService;
    @Autowired private MedicoService medicoService;

    // --- UM ÚNICO MÉTODO PARA FILA ---
    @GetMapping("/fila")
    public List<Agendamento> getFilaConsulta(Authentication authentication) {
        // Pega o objeto do usuário logado do token
        Profissional profissionalLogado = (Profissional) authentication.getPrincipal();

        // Se o usuário logado for ADMIN...
        if (profissionalLogado.getPerfil() == PerfilUsuario.ADMIN) {
            // ...chama o método que busca a fila de TODOS os médicos.
            return atendimentoService.listarFila(StatusAgendamento.AGUARDANDO_CONSULTA);
        } else {
            // ...senão (se for um Médico), busca apenas a sua própria fila.
            return medicoService.listarFilaDoMedico(profissionalLogado.getId());
        }
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
    public List<Consulta> getHistorico(@PathVariable Long pacienteId){
        return medicoService.getHistoricoPaciente(pacienteId);
    }

    @PostMapping("/atestado/{consultaId}")
    public String gerarAtestado(@PathVariable Long consultaId, @RequestParam Integer dias){
        return medicoService.gerarTextoAtestado(consultaId, dias);
    }
}