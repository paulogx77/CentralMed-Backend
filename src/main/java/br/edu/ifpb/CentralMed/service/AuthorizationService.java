package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private ProfissionalRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findUserDetailsByUsuarioLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas ou usuário '" + username + "' não encontrado."));
    }
}