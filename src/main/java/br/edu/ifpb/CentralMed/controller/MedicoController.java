package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.config.auditoria.Auditable; // Import da anotação de auditoria
import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.Consulta;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.Triagem;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.service.AtendimentoService;
import br.edu.ifpb.CentralMed.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    @Autowired private AtendimentoService atendimentoService;
    @Autowired private MedicoService medicoService;

    /**
     * Retorna a fila de pacientes aguardando consulta.
     * Se o usuário for ADMIN, retorna a fila geral de todos os médicos.
     * Se for um Médico, retorna apenas a sua própria fila de espera.
     */
    @GetMapping("/fila")
    public List<Agendamento> getFilaConsulta(Authentication authentication) {
        Profissional profissionalLogado = (Profissional) authentication.getPrincipal();

        if (profissionalLogado.getPerfil() == PerfilUsuario.ADMIN) {
            return atendimentoService.listarFila(StatusAgendamento.AGUARDANDO_CONSULTA);
        } else {
            return medicoService.listarFilaDoMedico(profissionalLogado.getId());
        }
    }

    /**
     * Busca os dados da triagem de um paciente com base no ID do agendamento.
     */
    @GetMapping("/dados-triagem/{agendamentoId}")
    public Triagem getTriagem(@PathVariable Long agendamentoId) {
        return medicoService.buscarDadosTriagem(agendamentoId);
    }

    /**
     * Inicia o atendimento de um paciente, criando um registro de consulta.
     */
    @PostMapping("/consulta/{agendamentoId}/iniciar")
    public Consulta iniciar(@PathVariable Long agendamentoId) {
        return medicoService.iniciarConsulta(agendamentoId);
    }

    /**
     * Finaliza a consulta, registrando anamnese, CID e insumos utilizados.
     */
    @PostMapping("/consulta/{consultaId}/finalizar")
    public Consulta finalizar(@PathVariable Long consultaId, @RequestBody FinalizarConsultaDTO dto) {
        return medicoService.finalizarConsulta(consultaId, dto);
    }

    /**
     * Retorna o histórico de consultas de um paciente.
     * Este endpoint é auditado para conformidade com a LGPD.
     */
    @GetMapping("/historico/{pacienteId}")
    @Auditable
    public List<Consulta> getHistorico(@PathVariable Long pacienteId){
        return medicoService.getHistoricoPaciente(pacienteId);
    }

    /**
     * Gera um texto simples para atestado médico.
     */
    @PostMapping("/atestado/{consultaId}")
    public String gerarAtestado(@PathVariable Long consultaId, @RequestParam Integer dias){
        return medicoService.gerarTextoAtestado(consultaId, dias);
    }
}