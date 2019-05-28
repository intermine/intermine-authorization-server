package org.intermine.security.authserver.service;

import org.intermine.security.authserver.model.AuthUserDetail;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailRepository userDetailRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Users> optionalUser=userDetailRepository.findByUsername(name);
        optionalUser.orElseThrow(()-> new UsernameNotFoundException("Username or password wrong"));
        UserDetails userDetails =new AuthUserDetail(optionalUser.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}
