package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.AgendamentoImediatoDTO;
import br.edu.ifpb.CentralMed.dto.TriagemDTO;
import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.Paciente;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.Triagem;
import br.edu.ifpb.CentralMed.model.enums.Prioridade;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.AgendamentoRepository;
import br.edu.ifpb.CentralMed.repository.PacienteRepository;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import br.edu.ifpb.CentralMed.repository.TriagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AtendimentoService {

    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private TriagemRepository triagemRepository;
    // O repository do painel foi removido daqui

    public Agendamento criarAgendamentoFuturo(Agendamento ag) {
        ag.setStatus(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(ag);
    }

    public Agendamento criarAtendimentoImediato(AgendamentoImediatoDTO dto) {
        Paciente p = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Profissional m = null;
        if (dto.getMedicoId() != null) {
            m = profissionalRepository.findById(dto.getMedicoId()).orElse(null);
        }
        Agendamento ag = new Agendamento();
        ag.setPaciente(p);
        ag.setMedico(m);
        ag.setData(LocalDate.now());
        ag.setHora(LocalTime.now());
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        ag.setSenhaPainel("S-" + (agendamentoRepository.count() + 1));
        if (dto.getPrioridade() != null) {
            ag.setPrioridade(Prioridade.valueOf(dto.getPrioridade()));
        }
        return agendamentoRepository.save(ag);
    }

    public Agendamento realizarCheckInAgendado(Long id) {
        Agendamento ag = agendamentoRepository.findById(id).orElseThrow();
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        if (ag.getSenhaPainel() == null || ag.getSenhaPainel().isEmpty()) {
            ag.setSenhaPainel("S-" + id);
        }
        return agendamentoRepository.save(ag);
    }

    public void cancelarAgendamento(Long id) {
        agendamentoRepository.findById(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CANCELADO);
            agendamentoRepository.save(ag);
        });
    }

    // O MÉTODO chamarPacientePainel FOI REMOVIDO DESTA CLASSE

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

    @Transactional
    public Triagem realizarTriagem(Long agendamentoId, TriagemDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado para triagem"));
        Profissional enfermeiro = profissionalRepository.findById(dto.getEnfermeiroId())
                .orElseThrow(() -> new RuntimeException("Profissional (Enfermeiro) não encontrado"));

        Triagem triagem = new Triagem();
        triagem.setPeso(dto.getPeso());
        triagem.setAltura(dto.getAltura());
        triagem.setPressao(dto.getPressao());
        triagem.setTemperatura(dto.getTemperatura());
        triagem.setSaturacao(dto.getSaturacao());
        triagem.setObservacoes(dto.getObservacoes());
        triagem.setAgendamento(agendamento);
        triagem.setEnfermeiro(enfermeiro);

        agendamento.setStatus(StatusAgendamento.AGUARDANDO_CONSULTA);
        agendamentoRepository.save(agendamento);

        return triagemRepository.save(triagem);
    }

    public List<Agendamento> listarFila(StatusAgendamento status) {
        return agendamentoRepository.findByDataAndStatusOrderByPrioridadeDescHoraAsc(LocalDate.now(), status);
    }
}