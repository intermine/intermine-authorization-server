package org.intermine.security.authserver.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

/**
 * This class implements spring social user detail service
 * and override the method to return custom details of the
 * user.
 *
 *
 * @author Rahul Yadav
 *
 */
@Service
public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

    /**
     * An object UserDetailsService used to load the the user
     * from the database with the help of username.
     */
    @Autowired
    private UserDetailsService userDetailService;

    /**
     * <p>This method load the user from the database using
     * user's unqiue username.
     *  </p>
     *
     * @param userName user to load
     * @return Object of spring SocialUserDetails
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userName) throws UsernameNotFoundException, DataAccessException {
        System.out.println("SocialUserDetailsServiceImpl.loadUserByUserId=" + userName);
        UserDetails userDetails = ((UserDetailServiceImpl) userDetailService).loadUserByUsername(userName);
        return (SocialUserDetailsImpl) userDetails;
    }

}