package org.intermine.security.authserver.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Model class for users table.
 *
 * @author Rahul Yadav
 *
 */
@Entity
@Table(name = "users")
@Data
public class Users implements Serializable {

    /**
     * Construct an user.
     */
    public Users() {}

    /**
     * <p>Construct an authenticatedUser.
     * </p>
     *
     * @param user instance of Users
     */
    public Users(Users user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.roles = user.getRoles();
        this.name = user.getName();
        this.userId = user.getUserId();

    }

    /**
     *Auto generated user_id of table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /**
     *username of user
     */
    @Column(name = "username")
    private String username;

    /**
     * Password set by user
     */
    @Column(name = "password")
    private String password;

    /**
     *Email registered by user
     */
    @Column(name = "email")
    private String email;

    /**
     *User account enabled status
     */
    @Column(name = "enabled")
    private boolean enabled;

    /**
     *User account expire status
     */
    @Column(name = "accountNonExpired")
    private boolean accountNonExpired;

    /**
     *User credentials expire status
     */
    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired;

    /**
     *User account lock status
     */
    @Column(name = "accountNonLocked")
    private boolean accountNonLocked;

    /**
     * Name of the user
     */
    @Column(name = "name", nullable = true)
    private String name;

    /**
     *Role user mapping
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "id", referencedColumnName = "id")})
    private List<Role> roles;


    /**
     * <p>Used to get unique id of the user.
     * </p>
     *
     * @return Integer id of the user
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * <p>Used to set user unqiue id.
     * </p>
     *
     * @param userId Id to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * <p>Used to get username of the user.
     * </p>
     *
     * @return String username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Used to set username of the user.
     * </p>
     *
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Used to get password of the user.
     * </p>
     *
     * @return String encrypted password
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Used to set user account password.
     * </p>
     *
     * @param password encrypted password to update
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <p>Used to get email registered by user.
     * </p>
     *
     * @return String registered email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * <p>Used to set email address of user.
     * </p>
     *
     * @param email Email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * <p>Used to check enabled status of
     * user account.
     * </p>
     *
     * @return boolean true if enabled else false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Used to set user account enabled status.
     * </p>
     *
     * @param enabled status to update
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>Used to check expire status of the user.
     * </p>
     *
     * @return boolean false if expired else true
     */
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * <p>Used to set user account expire status.
     * </p>
     *
     * @param accountNonExpired status to update
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * <p>Used to user credential expire status.
     * </p>
     *
     * @return boolean false if expired else true
     */
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * <p>Used to set user credentials expire status.
     * </p>
     *
     * @param credentialsNonExpired status to update
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * <p>Used to check lock status of user account
     * </p>
     *
     * @return false if locked else true
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * <p>Used to set user account lock status.
     * </p>
     *
     * @param accountNonLocked status to update
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * <p>Used to get roles a user have.
     * </p>
     *
     * @return List of roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * <p>Used to set roles a user have.
     * </p>
     *
     * @param roles list of roles a user have
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * <p>Used to get name of the user.
     * </p>
     *
     * @return String name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Used to set name of the user.
     * </p>
     *
     * @param name Full name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

}