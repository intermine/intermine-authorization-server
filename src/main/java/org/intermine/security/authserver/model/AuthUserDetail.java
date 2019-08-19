package org.intermine.security.authserver.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class extends Users model and implements spring
 * security default UserDetails interface.
 *
 * @author Rahul Yadav
 *
 */
public class AuthUserDetail extends Users implements UserDetails {

    /**
     * Construct an AuthUserDetail model instance.
     */
    public AuthUserDetail(Users user) {
        super(user);
    }

    /**
     * <p>Used to get granted authorities of user.
     * It will return grantedAuthorities based on
     * the roles that user have.
     * </p>
     *
     * @return collection list of grantedAuthorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        getRoles().forEach(role -> { grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPermissions().forEach(permission -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
            });

        });
        return grantedAuthorities;
    }

    /**
     * <p>Used to get password of user.
     * </p>
     *
     * @return String encrypted password of user
     */
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    /**
     * <p>Used to get username of user.
     * </p>
     *
     * @return String unique username
     */
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    /**
     * <p>Used to check account expire status.
     * </p>
     *
     * @return boolean false if already expire else true
     */
    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    /**
     * <p>Used to check account lock status.
     * </p>
     *
     * @return boolean false if locked else true
     */
    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    /**
     * <p>Used to check credentials expire status.
     * </p>
     *
     * @return boolean false if already expire else true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    /**
     * <p>Used to check account enable status.
     * </p>
     *
     * @return boolean false if not enabled else true
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
