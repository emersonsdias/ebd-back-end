package br.com.emersondias.ebd.security.models;

import br.com.emersondias.ebd.entities.User;
import br.com.emersondias.ebd.entities.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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

    public UserAuthenticated(User user) {
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
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public boolean hasRole(UserRole role) {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role.getDescription()));
    }
}
