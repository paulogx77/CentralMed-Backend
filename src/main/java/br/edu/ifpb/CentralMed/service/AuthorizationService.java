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
    ProfissionalRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // --- CORREÇÃO AQUI ---
        // Buscamos o usuário. Se vier nulo, lançamos a exceção.
        UserDetails user = repository.findByUsuarioLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário '" + username + "' não encontrado.");
        }

        return user;
        // ---------------------
    }
}