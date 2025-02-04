package br.com.emersondias.ebd.security.models;

import br.com.emersondias.ebd.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserAuthenticated implements UserDetails {

    @Getter
    private final UUID id;
    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;
    @Getter
    private final String name;

    private UserAuthenticated(User user) {
        super();
        id = user.getId();
        username = user.getEmail();
        password = user.getPassword();
        active = user.isActive();
        authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getDescription())).toList();
        name = user.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

}
