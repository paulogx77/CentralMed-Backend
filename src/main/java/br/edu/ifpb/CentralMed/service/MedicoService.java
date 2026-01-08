package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.*;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {
    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private ConsumoInsumoRepository consumoRepository;
    @Autowired private TriagemRepository triagemRepository;

    // --- Métodos Existentes ---

    public Triagem buscarDadosTriagem(Long agendamentoId) {
        return triagemRepository.findByAgendamentoId(agendamentoId);
    }

    public Consulta iniciarConsulta(Long agendamentoId) {
        Agendamento ag = agendamentoRepository.findById(agendamentoId).orElseThrow();
        ag.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        agendamentoRepository.save(ag);

        Consulta c = new Consulta();
        c.setAgendamento(ag);
        c.setDataHoraInicio(LocalDateTime.now());
        return consultaRepository.save(c);
    }

    @Transactional
    public Consulta finalizarConsulta(Long consultaId, FinalizarConsultaDTO dto) {
        Consulta c = consultaRepository.findById(consultaId).orElseThrow();

        // Dados Clínicos
        c.setAnamnese(dto.getAnamnese());
        c.setDiagnosticoCid10(dto.getDiagnosticoCid10());
        c.setPrescricao(dto.getPrescricao());
        c.setDataHoraFim(LocalDateTime.now());

        // Baixa de Estoque
        if(dto.getInsumosConsumidos() != null && !dto.getInsumosConsumidos().isEmpty()) {
            List<ConsumoInsumo> listaConsumo = new ArrayList<>();

            for(InsumoRequestDTO item : dto.getInsumosConsumidos()) {
                EstoqueInsumos insumoEstoque = estoqueRepository.findById(item.getInsumoId())
                        .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

                // Subtrai do estoque
                insumoEstoque.setQtdeAtual(insumoEstoque.getQtdeAtual() - item.getQuantidade());
                estoqueRepository.save(insumoEstoque);

                // Cria registro de consumo
                ConsumoInsumo consumo = new ConsumoInsumo();
                consumo.setConsulta(c);
                consumo.setInsumo(insumoEstoque);
                consumo.setQuantidadeUtilizada(item.getQuantidade());
                listaConsumo.add(consumo);
            }
            c.setInsumosConsumidos(listaConsumo);
        }

        c.getAgendamento().setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(c.getAgendamento());

        return consultaRepository.save(c);
    }

    // --- NOVOS MÉTODOS (Para completar o Diagrama) ---

    public List<Consulta> getHistoricoPaciente(Long pacienteId) {
        // Busca apenas as consultas que foram FINALIZADAS
        return consultaRepository.buscarHistoricoDoPaciente(
                pacienteId,
                StatusAgendamento.FINALIZADO
        );
    }

    public String gerarTextoAtestado(Long consultaId, Integer diasAfastamento) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        String paciente = c.getAgendamento().getPaciente().getNome();
        String medico = c.getAgendamento().getMedico().getNome();
        String crm = c.getAgendamento().getMedico().getCrmRegistro();

        String data = c.getDataHoraFim() != null
                ? c.getDataHoraFim().toLocalDate().toString()
                : LocalDateTime.now().toLocalDate().toString();

        return String.format(
                "ATESTADO MÉDICO\n\n" +
                        "Atesto para os devidos fins que o(a) Sr(a). %s foi atendido(a) por mim nesta data (%s) " +
                        "e necessita de %d dias de afastamento de suas atividades laborais.\n\n" +
                        "Dr. %s - CRM: %s",
                paciente, data, diasAfastamento, medico, crm
        );
    }
}