package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.LoginDTO;
import br.edu.ifpb.CentralMed.dto.LoginResponseDTO;
import br.edu.ifpb.CentralMed.dto.RegisterDTO;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import br.edu.ifpb.CentralMed.service.TokenService;
import jakarta.validation.Valid; // Boa prática para validar DTOs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException; // Captura erro de login específico
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ProfissionalRepository repository;
    @Autowired private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var auth = authenticationManager.authenticate(usernamePassword);

            var token = tokenService.gerarToken((Profissional) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {

            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {

        if(this.repository.findByUsuarioLogin(data.usuarioLogin()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

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