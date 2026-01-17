package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.Anexo;
import br.edu.ifpb.CentralMed.repository.AnexoRepository;
import br.edu.ifpb.CentralMed.repository.ConsultaRepository;
import br.edu.ifpb.CentralMed.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/anexos")
public class AnexoController {
    @Autowired private FileStorageService fileStorageService;
    @Autowired private AnexoRepository anexoRepository;
    @Autowired private ConsultaRepository consultaRepository;

    @PostMapping("/upload/{consultaId}")
    public ResponseEntity<Anexo> uploadFile(@RequestParam("file") MultipartFile file,
                                            @PathVariable Long consultaId) {

        String fileName = fileStorageService.storeFile(file);

        Anexo anexo = new Anexo();
        anexo.setNomeOriginal(file.getOriginalFilename());
        anexo.setNomeArquivo(fileName);
        anexo.setTamanho(file.getSize());
        anexo.setTipo(file.getContentType());
        anexo.setDataUpload(LocalDateTime.now());
        anexo.setConsulta(consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada")));

        anexoRepository.save(anexo);

        return ResponseEntity.ok(anexo);
    }
}