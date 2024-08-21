package com.aklimets.pet.domain.dto.authentication;

import com.aklimets.pet.domain.dto.authentication.attribute.UserIdNumber;
import com.aklimets.pet.model.attribute.Username;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

@Getter
public final class UserAuthentication implements Authentication {

    private final UserIdNumber id;

    private final Username username;

    private final Collection<? extends GrantedAuthority> authorities;

    private boolean authenticated;

    public UserAuthentication(UserIdNumber id, Username username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        Assert.isTrue(!authenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String toString() {
        return "UserAuthentication{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", authorities=" + authorities +
                ", authenticated=" + authenticated +
                '}';
    }
}
