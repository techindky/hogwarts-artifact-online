package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {

    private final HogwartsUser hogwartsUser;

    public MyUserPrincipal(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(hogwartsUser.getRoles()," "))
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .toList();
    }

    @Override
    public String getPassword() {
        return hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hogwartsUser.getUsername();
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
        return hogwartsUser.isEnabled();
    }
    public HogwartsUser getHogwartsUser(){
        return hogwartsUser;
    }
}
