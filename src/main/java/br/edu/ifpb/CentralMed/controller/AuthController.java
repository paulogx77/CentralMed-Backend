package br.edu.ifpb.CentralMed.controller;


import br.edu.ifpb.CentralMed.dto.LoginDTO;
import br.edu.ifpb.CentralMed.dto.LoginResponseDTO;
import br.edu.ifpb.CentralMed.dto.RegisterDTO;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import br.edu.ifpb.CentralMed.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ProfissionalRepository repository;
    @Autowired private TokenService tokenService;

    // AuthController.java

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var auth = authenticationManager.authenticate(usernamePassword); // <--- AQUI PODE ESTOURAR A PILHA

            var token = tokenService.gerarToken((Profissional) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            e.printStackTrace(); // Veja no console onde estourou
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if(this.repository.findByUsuarioLogin(data.usuarioLogin()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Profissional newUser = new Profissional();
        newUser.setNome(data.nome());
        newUser.setUsuarioLogin(data.usuarioLogin());
        newUser.setSenha(encryptedPassword);
        newUser.setCargo(data.cargo());
        newUser.setPerfil(data.perfil());
        newUser.setCrmRegistro(data.crmRegistro());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
