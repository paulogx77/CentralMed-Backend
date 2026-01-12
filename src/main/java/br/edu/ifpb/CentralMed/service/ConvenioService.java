package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Convenio;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvenioService {

    @Autowired
    private ConvenioRepository convenioRepository;

    /**
     * Salva um novo convênio.
     * @param convenio O objeto Convenio a ser salvo.
     * @return O convênio salvo com o ID gerado.
     */
    public Convenio salvarConvenio(Convenio convenio) {
        // Validação futura: verificar se registro ANS ou CNPJ já existem antes de salvar.
        return convenioRepository.save(convenio);
    }

    /**
     * Lista todos os convênios cadastrados.
     * @return Uma lista de todos os convênios.
     */
    public List<Convenio> listarConvenios() {
        return convenioRepository.findAll();
    }

    /**
     * Atualiza os dados de um convênio existente.
     * @param id O ID do convênio a ser atualizado.
     * @param dados O objeto Convenio com os novos dados.
     * @return O convênio atualizado.
     * @throws RuntimeException se o convênio não for encontrado.
     */
    public Convenio atualizarConvenio(Long id, Convenio dados) {
        Convenio convenioExistente = convenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com o ID: " + id));

        convenioExistente.setNome(dados.getNome());
        convenioExistente.setCnpj(dados.getCnpj());
        convenioExistente.setRegistroAns(dados.getRegistroAns());

        return convenioRepository.save(convenioExistente);
    }

    /**
     * Deleta um convênio pelo ID.
     * @param id O ID do convênio a ser deletado.
     * @throws RuntimeException se o convênio não for encontrado.
     */
    public void deletarConvenio(Long id) {
        if (!convenioRepository.existsById(id)) {
            throw new RuntimeException("Convênio não encontrado com o ID: " + id);
        }
        convenioRepository.deleteById(id);
    }
}