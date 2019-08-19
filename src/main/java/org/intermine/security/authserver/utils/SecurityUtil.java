package org.intermine.security.authserver.utils;

import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.service.SocialUserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialUserDetails;

import java.util.List;

/**
 * SecurityUtil logged in user based on the type of
 * role that user have and set authentication
 * on current securityContext.
 *
 * @author Rahul Yadav
 *
 */
public class SecurityUtil {

    /**
     * <p>Logged in user based on role and authorities.
     * </p>
     *
     * @param user Instance of Users model class.
     * @param roleNames list of roles name that user have
     */
    public static void logInUser(Users user, List<String> roleNames) {
        SocialUserDetails userDetails = new SocialUserDetailsImpl(user, roleNames);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
