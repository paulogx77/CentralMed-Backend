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

import java.time.LocalDateTime;
import java.util.ArrayList; // <--- FALTAVA ESSE
import java.util.List;      // <--- FALTAVA ESSE

@Service
public class MedicoService {
    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private ConsumoInsumoRepository consumoRepository;
    @Autowired private TriagemRepository triagemRepository;

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
            // Correção aqui: Uso do Diamond Operator <>
            List<ConsumoInsumo> listaConsumo = new ArrayList<>();

            for(InsumoRequestDTO item : dto.getInsumosConsumidos()) {
                EstoqueInsumos insumoEstoque = estoqueRepository.findById(item.getInsumoId()).orElseThrow();

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

        // Atualiza status do agendamento
        c.getAgendamento().setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(c.getAgendamento());

        return consultaRepository.save(c);
    }
}