package org.intermine.security.authserver.service;

import org.intermine.security.authserver.dao.AppRoleDAO;
import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.model.AuthUserDetail;
import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private AppUserDAO appUserDAO;

    @Autowired
    private AppRoleDAO appRoleDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("UserDetailsServiceImpl.loadUserByUsername=" + userName);
        Users users = this.appUserDAO.findAppUserByUserName(userName);

        if (users == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        System.out.println("Found User: " + users);

        List<Role> roleNames= users.getRoles();

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        List<String> rolesList=new ArrayList<>();
        if (roleNames != null) {
            for (Role role : roleNames) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                grantList.add(authority);
                rolesList.add(role.getName());
            }
        }

        SocialUserDetailsImpl userDetails = new SocialUserDetailsImpl(users, rolesList);

        return userDetails;

    }
}
