package org.alpha.omega.hogwarts_artifacts_online.configuration.security;

import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * This class is used as an Adapter Pattern for return an UserDetails class in loadUserByUsername method of the UserService class
 */
public record MyUserPrincipal(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isEnabled() {  return this.user.getEnabled(); }
}
