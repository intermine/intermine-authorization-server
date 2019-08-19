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

/**
 * This class extends spring default oauth2 User details
 * service and overrides the default methods according
 * to Intermine Authorization server requirements.
 *
 *
 * @author Rahul Yadav
 *
 */
@Service("userDetailsService")
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    /**
     * An object of jpa repository to query users table in database.
     */
    @Autowired
    UserDetailRepository userDetailRepository;

    /**
     * Used to query users database.
     */
    @Autowired
    private AppUserDAO appUserDAO;

    /**
     * Used to query role database.
     */
    @Autowired
    private AppRoleDAO appRoleDAO;

    /**
     * <p>This method is used to load the the user with the
     * help of unique username of the user.
     *  </p>
     *
     * @param userName user to load
     * @return Object of userDetails
     */
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
