package org.intermine.security.authserver.service;


import org.intermine.security.authserver.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SocialUserDetailsImpl implements SocialUserDetails {

    private static final long serialVersionUID = 1L;
    private List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
    private Users users;

    public SocialUserDetailsImpl(Users users, List<String> roleNames) {
        this.users = users;

        for (String roleName : roleNames) {

            GrantedAuthority grant = new SimpleGrantedAuthority(roleName);
            this.list.add(grant);
        }
    }

    @Override
    public String getUserId() {
        return this.users.getUserId() + "";
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return list;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
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
        return true;
    }

}
