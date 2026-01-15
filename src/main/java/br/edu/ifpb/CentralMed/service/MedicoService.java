package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.model.enums.StatusGuia;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private TriagemRepository triagemRepository;
    @Autowired private GuiaConsultaRepository guiaRepository;
    @Autowired private ConvenioRepository convenioRepository;

    public Triagem buscarDadosTriagem(Long agendamentoId) {
        return triagemRepository.findByAgendamentoId(agendamentoId);
    }

    @Transactional
    public Consulta iniciarConsulta(Long agendamentoId) {
        Agendamento ag = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        ag.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        agendamentoRepository.save(ag);

        Consulta c = new Consulta();
        c.setAgendamento(ag);
        c.setDataHoraInicio(LocalDateTime.now());
        return consultaRepository.save(c);
    }

    @Transactional
    public Consulta finalizarConsulta(Long consultaId, FinalizarConsultaDTO dto) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        
        c.setAnamnese(dto.getAnamnese());
        c.setDiagnosticoCid10(dto.getDiagnosticoCid10());
        c.setPrescricao(dto.getPrescricao());
        c.setDataHoraFim(LocalDateTime.now());

        c.getAgendamento().setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(c.getAgendamento());

        Paciente paciente = c.getAgendamento().getPaciente();
        if (paciente.getConvenio() != null && !paciente.getConvenio().equalsIgnoreCase("Particular")) {
            convenioRepository.findByNome(paciente.getConvenio()).ifPresent(convenio -> {
                GuiaConsulta guia = new GuiaConsulta();
                guia.setConsulta(c);
                guia.setConvenio(convenio);
                guia.setNumeroGuia("G" + LocalDate.now().getYear() + "-" + c.getId());
                guia.setDataEmissao(LocalDate.now());
                guia.setStatus(StatusGuia.ABERTA);
                guia.setValorConsulta(new BigDecimal("120.00"));
                guiaRepository.save(guia);
            });
        }
        return consultaRepository.save(c);
    }

    public List<Agendamento> listarFilaDoMedico(Long medicoId) {
        return agendamentoRepository.findByMedicoIdAndDataAndStatusOrderByPrioridadeDescHoraAsc(
                medicoId,
                LocalDate.now(),
                StatusAgendamento.AGUARDANDO_CONSULTA
        );
    }

    public List<Consulta> getHistoricoPaciente(Long pacienteId) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getAgendamento() != null &&
                             c.getAgendamento().getPaciente() != null &&
                             c.getAgendamento().getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public String gerarTextoAtestado(Long consultaId, Integer diasAfastamento) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        String nomePaciente = c.getAgendamento().getPaciente().getNome();
        return "Atesto que o paciente " + nomePaciente + " necessita de " + diasAfastamento + " dias de repouso.";
    }
}