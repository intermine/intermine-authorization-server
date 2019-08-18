package org.intermine.security.authserver.service;


import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.model.Users;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.security.NoSuchAlgorithmException;

/**
 * This class implements ConnectionSignUp interface
 * of spring social connect.
 * ConnectionSignUpImpl class can be used to store the user
 * information in Authorization server when user logged in
 * using 3rd party oauth provider.
 *
 * @author Rahul Yadav
 *
 */
public class ConnectionSignUpImpl implements ConnectionSignUp {

    /**
     * Used to query users database.
     */
    private AppUserDAO appUserDAO;

    /**
     * <p>Sets appUserDAO value.
     * </p>
     *
     * @param appUserDAO AppUserDAP instance
     */
    public ConnectionSignUpImpl(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    /**
     * <p>Creates a new account of user who logged in using
     * 3rd party OAuth provider on IM auth server.
     * </p>
     *
     * @param connection
     * @return String username of user
     */
    @Override
    public String execute(Connection<?> connection) {

        Users account = null;
        try {
            account = appUserDAO.createAppUser(connection);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return account.getUsername();
    }

}
