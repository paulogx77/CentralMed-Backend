package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.AgendamentoImediatoDTO;
import br.edu.ifpb.CentralMed.dto.TriagemDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.Prioridade;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AtendimentoService {

    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private TriagemRepository triagemRepository;
    @Autowired private ConsultaRepository consultaRepository;

    private static final int PRAZO_RETORNO_DIAS = 30;

    public Agendamento criarAgendamentoFuturo(Agendamento ag) {
        if (ag.getPaciente() != null && ag.getMedico() != null) {
            boolean isRetorno = verificarSeERetorno(ag.getPaciente().getId(), ag.getMedico().getId());
            ag.setRetorno(isRetorno);
        }
        ag.setStatus(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(ag);
    }

    public Agendamento criarAtendimentoImediato(AgendamentoImediatoDTO dto) {
        Paciente p = pacienteRepository.findById(dto.getPacienteId()).orElseThrow();
        Profissional m = null;
        if(dto.getMedicoId() != null) {
            m = profissionalRepository.findById(dto.getMedicoId()).orElse(null);
        }

        Agendamento ag = new Agendamento();
        ag.setPaciente(p);
        ag.setMedico(m);
        ag.setData(LocalDate.now());
        ag.setHora(LocalTime.now());
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        ag.setSenhaPainel("SENHA-" + System.currentTimeMillis() % 1000);

        if (dto.getPrioridade() != null) {
            ag.setPrioridade(Prioridade.valueOf(dto.getPrioridade()));
        }

        if (ag.getPaciente() != null && ag.getMedico() != null) {
            boolean isRetorno = verificarSeERetorno(ag.getPaciente().getId(), ag.getMedico().getId());
            ag.setRetorno(isRetorno);
        }

        return agendamentoRepository.save(ag);
    }

    public Agendamento realizarCheckInAgendado(Long id) {
        Agendamento ag = agendamentoRepository.findById(id).orElseThrow();
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        ag.setSenhaPainel("SENHA-" + ag.getId());
        return agendamentoRepository.save(ag);
    }

    public void cancelarAgendamento(Long id) {
        Agendamento ag = agendamentoRepository.findById(id).orElseThrow();
        ag.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(ag);
    }

    public Agendamento chamarPacientePainel(Long id) {
        return agendamentoRepository.findById(id).orElseThrow();
    }

    public Paciente atualizarPaciente(Long id, Paciente dadosNovos) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        pacienteExistente.setNome(dadosNovos.getNome());
        pacienteExistente.setCpf(dadosNovos.getCpf());
        pacienteExistente.setDataNasc(dadosNovos.getDataNasc());
        pacienteExistente.setConvenio(dadosNovos.getConvenio());
        pacienteExistente.setAlergiasComorbidades(dadosNovos.getAlergiasComorbidades());
        return pacienteRepository.save(pacienteExistente);
    }

    // --- O RETORNO QUE ESTAVA FALTANDO ERA DESTE MÉTODO ---
    public Triagem realizarTriagem(Long agendamentoId, TriagemDTO dto) {
        Agendamento ag = agendamentoRepository.findById(agendamentoId).orElseThrow();
        Profissional enf = profissionalRepository.findById(dto.getEnfermeiroId()).orElseThrow();

        Triagem t = new Triagem();
        t.setPeso(dto.getPeso());
        t.setAltura(dto.getAltura());
        t.setPressao(dto.getPressao());
        t.setTemperatura(dto.getTemperatura());
        t.setSaturacao(dto.getSaturacao());
        t.setObservacoes(dto.getObservacoes());
        t.setAgendamento(ag);
        t.setEnfermeiro(enf);

        ag.setStatus(StatusAgendamento.AGUARDANDO_CONSULTA);
        agendamentoRepository.save(ag);

        return triagemRepository.save(t); // Retorno estava faltando
    }

    public List<Agendamento> listarFila(StatusAgendamento status) {
        return agendamentoRepository.findByDataAndStatusOrderByPrioridadeDescHoraAsc(LocalDate.now(), status);
    }

    private boolean verificarSeERetorno(Long pacienteId, Long medicoId) {
        if (pacienteId == null || medicoId == null) return false;

        List<Consulta> ultimasConsultas = consultaRepository
                .findUltimaConsultaPorPacienteEMedico(pacienteId, medicoId);

        if (ultimasConsultas.isEmpty()) return false;

        Consulta ultimaConsulta = ultimasConsultas.get(0);
        LocalDateTime dataUltimaConsulta = ultimaConsulta.getDataHoraInicio();

        return dataUltimaConsulta.plusDays(PRAZO_RETORNO_DIAS).isAfter(LocalDateTime.now());
    }
}