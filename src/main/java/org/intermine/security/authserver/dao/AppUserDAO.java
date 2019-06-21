package org.intermine.security.authserver.dao;


import org.intermine.security.authserver.form.AppUserForm;
import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.intermine.security.authserver.security.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.crypto.keygen.KeyGenerators.secureRandom;

@Repository
@Transactional
public class AppUserDAO {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AppRoleDAO appRoleDAO;

    public Users findAppUserByUserId(Long userId) {
        try {
            String sql = "select e from " + Users.class.getName() + " e where e.user_id = :userId ";
            Query query = entityManager.createQuery(sql, Users.class);
            query.setParameter("userId", userId);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Users findAppUserByUserName(String userName) {
        try {
            String sql = "select e from " + Users.class.getName() + " e "
                    + " where e.username = :userName ";
            Query query = entityManager.createQuery(sql, Users.class);
            query.setParameter("userName", userName);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Users findByEmail(String email) {
        try {
            String sql = "select e from " + Users.class.getName() + " e "
                    + " where e.email = :email ";
            Query query = entityManager.createQuery(sql, Users.class);
            query.setParameter("email", email);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private String findAvailableUserName(String userName_prefix) {
        Users account = this.findAppUserByUserName(userName_prefix);
        if (account == null) {
            return userName_prefix;
        }
        int i = 0;
        while (true) {
            String userName = userName_prefix + "_" + i++;
            account = this.findAppUserByUserName(userName);
            if (account == null) {
                return userName;
            }
        }
    }

    // Auto create App User Account.
    public Users createAppUser(Connection<?> connection) throws NoSuchAlgorithmException {
        ConnectionKey key = connection.getKey();
        System.out.println("key= (" + key.getProviderId() + "," + key.getProviderUserId() + ")");
        UserProfile userProfile = connection.fetchUserProfile();
        String email = userProfile.getEmail();
        Users users = this.findByEmail(email);
        if (users != null) {
            return users;
        }
        String userName_prefix = userProfile.getFirstName().trim().toLowerCase()
                + "_" + userProfile.getLastName().trim().toLowerCase();

        String userName = this.findAvailableUserName(userName_prefix);
        String encrytedPassword =passwordEncoder().encode(Encryption.SHA1(secureRandom(16).generateKey()));
        users = new Users();
        users.setEnabled(true);
        users.setCredentialsNonExpired(true);
        users.setAccountNonLocked(true);
        users.setCredentialsNonExpired(true);
        users.setPassword(encrytedPassword);
        users.setUsername(userName);
        users.setEmail(email);
        users.setFirstName(userProfile.getFirstName());
        users.setLastName(userProfile.getLastName());
        this.entityManager.persist(users);
        // Create default Role
        List<String> roleNames = new ArrayList<String>();
        roleNames.add(Role.ROLE_USER);
        this.appRoleDAO.createRoleFor(users, roleNames);

        return users;
    }

    public Users registerNewUserAccount(AppUserForm appUserForm, List<String> roleNames) {
        Users users = new Users();
        users.setUsername(appUserForm.getUserName());
        users.setEmail(appUserForm.getEmail());
        users.setFirstName(appUserForm.getFirstName());
        users.setLastName(appUserForm.getLastName());
        users.setEnabled(true);
        String encrytedPassword = passwordEncoder().encode(appUserForm.getPassword());
        users.setPassword(encrytedPassword);
        this.entityManager.persist(users);
        this.entityManager.flush();

        this.appRoleDAO.createRoleFor(users, roleNames);

        return users;
    }

    private PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
}