package br.edu.ifpb.CentralMed.config;

// ... (seus imports estão corretos)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {

                    // ===========================================
                    // 1. ROTAS PÚBLICAS (NÃO PRECISA DE LOGIN)
                    // ===========================================
                    // Login e Registro podem ser acessados por todos
                    req.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();
                    // Documentação da API (se usar Swagger)
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    // ===========================================
                    // 2. ROTAS DE ADMIN (SÓ ADMIN ACESSA)
                    // ===========================================
                    // Qualquer rota que comece com /api/admin/ só pode ser acessada por ADMIN
                    req.requestMatchers("/api/admin/**").hasRole("ADMIN");

                    // ===========================================
                    // 3. REGRAS MISTAS (Leitura vs. Escrita)
                    // ===========================================
                    // POST /api/convenios é restrito a ADMIN
                    req.requestMatchers(HttpMethod.POST, "/api/convenios").hasRole("ADMIN");
                    // GET /api/convenios é aberto para qualquer usuário logado
                    req.requestMatchers(HttpMethod.GET, "/api/convenios").authenticated();

                    // ===========================================
                    // 4. OUTRAS ROTAS (Qualquer logado pode usar)
                    // ===========================================
                    req.requestMatchers("/api/recepcao/**").authenticated();
                    req.requestMatchers("/api/estoque/**").authenticated();
                    req.requestMatchers("/api/triagem/**").authenticated();
                    req.requestMatchers("/api/medico/**").authenticated();
                    req.requestMatchers("/api/financeiro/**").authenticated();

                    // QUALQUER OUTRA ROTA que não foi definida acima precisa de autenticação.
                    // Isso é mais seguro que 'denyAll()', pois evita que você se tranque pra fora
                    // ao criar um endpoint novo e esquecer de liberar aqui.
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}