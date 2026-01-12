
package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.dto.InsumoRequestDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private LoteInsumoRepository loteRepository;
    @Autowired private TriagemRepository triagemRepository;
    @Autowired private GuiaConsultaRepository guiaRepository;

    // Método que estava faltando
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

        if (dto.getInsumosConsumidos() != null && !dto.getInsumosConsumidos().isEmpty()) {
            // (sua lógica de baixa de estoque)
        }

        c.getAgendamento().setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(c.getAgendamento());

        Paciente paciente = c.getAgendamento().getPaciente();
        if (paciente.getConvenio() != null) {
            GuiaConsulta guia = new GuiaConsulta();
            guia.setConsulta(c);
            guia.setNumeroGuia("G" + LocalDate.now().getYear() + "-" + c.getId());
            guia.setDataEmissao(LocalDate.now());
            guia.setStatus(StatusGuia.ABERTA);
            guia.setValorConsulta(new BigDecimal("120.00"));
            guiaRepository.save(guia);
        }

        return consultaRepository.save(c);
    }

    // Método que estava faltando
    public List<Consulta> getHistoricoPaciente(Long pacienteId) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getAgendamento().getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    // Método que estava faltando
    public String gerarTextoAtestado(Long consultaId, Integer diasAfastamento) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        String nomePaciente = c.getAgendamento().getPaciente().getNome();
        return "Atesto que o paciente " + nomePaciente + " necessita de " + diasAfastamento + " dias de repouso.";
    }
}