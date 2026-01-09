package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.*;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.Prioridade;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AtendimentoService {
    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private TriagemRepository triagemRepository;

    // --- AGENDAMENTO E RECEPÇÃO ---

    // 1. Agendar Futuro
    public Agendamento criarAgendamentoFuturo(Agendamento ag) {
        ag.setStatus(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(ag);
    }

    // 2. Chegou no balcão (Check-in Imediato)
    public Agendamento criarAtendimentoImediato(AgendamentoImediatoDTO dto) {
        Paciente p = pacienteRepository.findById(dto.getPacienteId()).orElseThrow();
        Profissional m = null;
        if(dto.getMedicoId() != null) {
            m = profissionalRepository.findById(dto.getMedicoId()).orElse(null);
        }

        Agendamento ag = new Agendamento();
        ag.setPaciente(p);
        ag.setMedico(m); // Pode ser null se for triagem geral
        ag.setData(LocalDate.now());
        ag.setHora(LocalTime.now());
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        ag.setSenhaPainel("SENHA-" + System.currentTimeMillis() % 1000);
        if (dto.getPrioridade() != null) {
            ag.setPrioridade(Prioridade.valueOf(dto.getPrioridade()));
        }

        return agendamentoRepository.save(ag);
    }

    // 3. Check-in de agendamento que já existia
    public Agendamento realizarCheckInAgendado(Long id) {
        Agendamento ag = agendamentoRepository.findById(id).orElseThrow();
        ag.setStatus(StatusAgendamento.AGUARDANDO_TRIAGEM);
        ag.setSenhaPainel("SENHA-" + ag.getId());
        return agendamentoRepository.save(ag);
    }

    // 4. Cancelar Agendamento (NOVO)
    public void cancelarAgendamento(Long id) {
        Agendamento ag = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        ag.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(ag);
    }

    // 5. Chamar Paciente no Painel (NOVO)
    public Agendamento chamarPacientePainel(Long id) {
        // Retorna o agendamento para que o Controller possa notificar o Front/TV
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    // --- GESTÃO DE PACIENTES ---

    // 6. Atualizar Cadastro de Paciente (NOVO)
    public Paciente atualizarPaciente(Long id, Paciente dadosNovos) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        p.setNome(dadosNovos.getNome());
        p.setConvenio(dadosNovos.getConvenio());
        p.setAlergiasComorbidades(dadosNovos.getAlergiasComorbidades());
        // CPF e Data de Nascimento geralmente não alteramos por aqui, mas pode adicionar se quiser

        return pacienteRepository.save(p);
    }

    // --- TRIAGEM (ENFERMAGEM) ---

    // 7. Realizar Triagem
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

        // Avança status para Médico
        ag.setStatus(StatusAgendamento.AGUARDANDO_CONSULTA);
        agendamentoRepository.save(ag);

        return triagemRepository.save(t);
    }

    // 8. Listar Filas
    public List<Agendamento> listarFila(StatusAgendamento status) {
        return agendamentoRepository.findByDataAndStatusOrderByPrioridadeDescHoraAsc(LocalDate.now(), status);
    }
}