package org.intermine.security.authserver.form;


import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;

/**
 * Model class of User Registration form.
 *
 * @author Rahul Yadav
 *
 */
public class AppUserForm {

    /**
     * Unique id of user
     */
    private Long userId;

    /**
     * Unique email of user
     */
    private String email;

    /**
     * unique username of user
     */
    private String userName;

    /**
     * Full name of user
     */
    private String name;

    /**
     * password of user
     */
    private String password;

    /**
     * Admin or normal user role
     */
    private String role;

    /**
     *Provider Name, if user logs in with 3rd party Oauth provider
     */
    private String signInProvider;

    /**
     *ProviderUserId, if user logs in with 3rd party Oauth provider
     */
    private String providerUserId;

    /**
     * Construct a blank user registration form.
     */
    public AppUserForm() {}

    /**
     * <p>Construct a user registration form when user tries to
     * login with 3rd party OAuth provider.
     * </p>
     *
     * @param connection current connection
     */
    public AppUserForm(Connection<?> connection) {
        UserProfile socialUserProfile = connection.fetchUserProfile();
        this.userId = null;
        this.email = socialUserProfile.getEmail();
        this.userName = socialUserProfile.getUsername();
        this.name = socialUserProfile.getName();

        ConnectionKey key = connection.getKey();
        this.signInProvider = key.getProviderId();
        this.providerUserId = key.getProviderUserId();
    }

    /**
     * <p>Used to get userId of user.
     * </p>
     *
     * @return userId unique Id of user
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * <p>Used to set type unique userid.
     * </p>
     *
     * @param id uniqueid of user
     */
    public void setUserId(Long id) {
        this.userId = id;
    }

    /**
     * <p>Used to get email address of user.
     * </p>
     *
     * @return email unique email address of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * <p>Used to set email address of user.
     * </p>
     *
     * @param email unqiue email address of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <p>Used to get username of user.
     * </p>
     *
     * @return username unique username of user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * <p>Used to set username of user.
     * </p>
     *
     * @param userName unique username of user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * <p>Used to get full name of user.
     * </p>
     *
     * @return name full name of user
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Used to set full name of user.
     * </p>
     *
     * @param name full name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Used to get password of user.
     * </p>
     *
     * @return password encrypted password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Used to set password of user.
     * </p>
     *
     * @param password password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <p>Used to get role type of user.
     * </p>
     *
     * @return role Admin or normal user role
     */
    public String getRole() {
        return role;
    }

    /**
     * <p>Used to set role type of user.
     * </p>
     *
     * @param role admin or normal user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * <p>Used to get name of 3rd party Oauth provider.
     * </p>
     *
     * @return signInprovider name of 3rd party Oauth provider
     */
    public String getSignInProvider() {
        return signInProvider;
    }

    /**
     * <p>Used to set 3rd party signInProvider.
     * </p>
     *
     * @param signInProvider provider name
     */
    public void setSignInProvider(String signInProvider) {
        this.signInProvider = signInProvider;
    }

    /**
     * <p>Used to get 3rd party providerId .
     * </p>
     *
     * @return providerUserId
     */
    public String getProviderUserId() {
        return providerUserId;
    }

    /**
     * <p>Used to set provider id.
     * </p>
     *
     * @param providerUserId providerUserId
     */
    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

}

