package org.intermine.security.authserver.utils;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Utility class to convert UserDetails object into String.
 *
 * @author Rahul Yadav
 *
 */
public class WebUtils {

    /**
     * <p>Converts spring default UserDetails obj
     * in to string.
     * </p>
     *
     * @param user UserDetails object
     * @return sb A string builder instance
     */
    public static String toString(UserDetails user) {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName:").append(user.getUsername());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
                if (first) {
                    sb.append(a.getAuthority());
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
}

