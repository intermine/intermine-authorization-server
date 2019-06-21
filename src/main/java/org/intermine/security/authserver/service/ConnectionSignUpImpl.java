package org.intermine.security.authserver.service;


import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.model.Users;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.security.NoSuchAlgorithmException;

public class ConnectionSignUpImpl implements ConnectionSignUp {

    private AppUserDAO appUserDAO;

    public ConnectionSignUpImpl(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

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
