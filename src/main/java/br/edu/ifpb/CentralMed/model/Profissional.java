package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.*; // Importe tudo do Lombok
import java.util.Collection;
import java.util.List;
import java.util.Objects; // Importante

@Getter // Apenas Getter
@Setter // Apenas Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profissional implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cargo;
    private String crmRegistro;

    @Column(unique = true)
    private String usuarioLogin;

    private String senha;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;

    // --- Métodos do Security ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.perfil == PerfilUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override public String getPassword() { return senha; }
    @Override public String getUsername() { return usuarioLogin; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // --- O SEGREDO PARA NÃO DAR STACKOVERFLOW ---
    // O Lombok @Data gera hashCode com TODOS os campos. O Security odeia isso.
    // Usamos apenas o ID aqui.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}