package org.intermine.security.authserver.service;


import org.intermine.security.authserver.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements spring socialUserDetails to
 * override some of getter and other methods.
 *
 * @author Rahul Yadav
 *
 */
public class SocialUserDetailsImpl implements SocialUserDetails {

    /**
     * Unique identifier for Serializable class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * New array list for granted Authorities.
     */
    private List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

    /**
     * An object of Users model class.
     */
    private Users users;

    /**
     * Construct an SocialUserDetailsImpl
     *
     * @param users object of Users model class
     * @param roleNames list of roles a user have
     */
    public SocialUserDetailsImpl(Users users, List<String> roleNames) {
        this.users = users;

        for (String roleName : roleNames) {

            GrantedAuthority grant = new SimpleGrantedAuthority(roleName);
            this.list.add(grant);
        }
    }

    /**
     * <p>Used to get unique id of the user
     * </p>
     *
     * @return String id of the user
     */
    @Override
    public String getUserId() {
        return this.users.getUserId() + "";
    }

    /**
     * <p>Used to get username of the user
     * </p>
     *
     * @return String username of the user
     */
    @Override
    public String getUsername() {
        return users.getUsername();
    }

    /**
     * <p>Used to get granted authorities of the user
     * </p>
     *
     * @return Collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return list;
    }

    /**
     * <p>Used to get password of the user.
     * </p>
     *
     * @return String encrypted password of the user
     */
    @Override
    public String getPassword() {
        return users.getPassword();
    }

    /**
     * <p>Used to user account expire status
     * </p>
     *
     * @return boolean false if expire else true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * <p>Used to check user account lock status
     * </p>
     *
     * @return boolean false if locked else true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * <p>Used to check user credentials expire status
     * </p>
     *
     * @return boolean false if expired else true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * <p>Used to check enabled status of user account
     * </p>
     *
     * @return boolean true if enabled else false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * <p>Used to get full name of the user.
     * </p>
     *
     * @return String name of the user
     */
    public String getName(){
        return users.getName();
    }

    /**
     * <p>Used to get email address of user.
     * </p>
     *
     * @return String unique email address of user
     */
    public String getEmail(){
        return users.getEmail();
    }


}
