package com.example.managingpromotions.services;

import com.example.managingpromotions.models.UserApp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private boolean enabled;

    public UserDetailsImpl(UserApp userApp) {
        this.username = userApp.getUsername();
        this.password = userApp.getPassword();
        this.enabled = userApp.isEnabled();
    }

    public UserDetailsImpl() {}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return enabled;
    }
}
