package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.FinalizarConsultaDTO;
import br.edu.ifpb.CentralMed.dto.InsumoRequestDTO;
import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.Consulta;
import br.edu.ifpb.CentralMed.model.ConsumoInsumo;
import br.edu.ifpb.CentralMed.model.LoteInsumo;
import br.edu.ifpb.CentralMed.model.Triagem;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import br.edu.ifpb.CentralMed.repository.AgendamentoRepository;
import br.edu.ifpb.CentralMed.repository.ConsultaRepository;
import br.edu.ifpb.CentralMed.repository.LoteInsumoRepository;
import br.edu.ifpb.CentralMed.repository.TriagemRepository;
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
    @Autowired private LoteInsumoRepository loteRepository;
    @Autowired private TriagemRepository triagemRepository;

    public Triagem buscarDadosTriagem(Long agendamentoId) {
        return triagemRepository.findByAgendamentoId(agendamentoId);
    }

    @Transactional
    public Consulta iniciarConsulta(Long agendamentoId) {
        Agendamento ag = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado para iniciar consulta"));

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
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada para finalizar"));

        c.setAnamnese(dto.getAnamnese());
        c.setDiagnosticoCid10(dto.getDiagnosticoCid10());
        c.setPrescricao(dto.getPrescricao());
        c.setDataHoraFim(LocalDateTime.now());

        List<ConsumoInsumo> listaConsumo = new ArrayList<>();

        if (dto.getInsumosConsumidos() != null && !dto.getInsumosConsumidos().isEmpty()) {
            for (InsumoRequestDTO itemConsumido : dto.getInsumosConsumidos()) {
                int qtdParaBaixar = itemConsumido.getQuantidade();
                if (qtdParaBaixar <= 0) continue;

                List<LoteInsumo> lotes = loteRepository.findLotesDisponiveisPorValidade(itemConsumido.getInsumoId());

                for (LoteInsumo lote : lotes) {
                    if (qtdParaBaixar <= 0) break;
                    if (lote.isVencido()) continue;

                    ConsumoInsumo consumo = new ConsumoInsumo();
                    consumo.setConsulta(c);
                    consumo.setInsumo(lote.getInsumo());

                    int qtdDisponivelNoLote = lote.getQuantidade();
                    int qtdBaixadaDoLote = Math.min(qtdParaBaixar, qtdDisponivelNoLote);

                    lote.setQuantidade(qtdDisponivelNoLote - qtdBaixadaDoLote);
                    consumo.setQuantidadeUtilizada(qtdBaixadaDoLote);
                    qtdParaBaixar -= qtdBaixadaDoLote;

                    loteRepository.save(lote);
                    listaConsumo.add(consumo);
                }

                if (qtdParaBaixar > 0) {
                    throw new RuntimeException("Estoque insuficiente para o insumo ID: " + itemConsumido.getInsumoId());
                }
            }
        }

        c.setInsumosConsumidos(listaConsumo);
        c.getAgendamento().setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(c.getAgendamento());

        return consultaRepository.save(c);
    }

    public List<Consulta> getHistoricoPaciente(Long pacienteId) {
        // Placeholder
        return new ArrayList<>();
    }

    public String gerarTextoAtestado(Long consultaId, Integer diasAfastamento) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada para gerar atestado"));

        String paciente = c.getAgendamento().getPaciente().getNome();
        String medico = c.getAgendamento().getMedico().getNome();
        String crm = c.getAgendamento().getMedico().getCrmRegistro();
        String data = LocalDateTime.now().toLocalDate().toString();

        return String.format(
                "ATESTADO MÉDICO\n\nAtesto, para os devidos fins, que o(a) paciente %s foi atendido(a) por mim nesta data (%s) " +
                        "e necessita de %d dias de afastamento de suas atividades laborais.\n\n" +
                        "Dr(a). %s\nCRM: %s",
                paciente, data, diasAfastamento, medico, crm
        );
    }
}