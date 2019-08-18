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

/**
 * Data access object class having access to database and
 * contains logic to access data from users table.
 *
 * @author Rahul Yadav
 *
 */
@Repository
@Transactional
public class AppUserDAO {

    /**
     * Used to read, delete and write an entity.
     * An object referenced by an entity is managed
     * by entity manager.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * An object of AppRoleDAO class used to create role
     * for user.
     */
    @Autowired
    private AppRoleDAO appRoleDAO;

    /**
     * <p>Find user by user unique id.
     * </p>
     *
     * @param userId unique id of user
     * @return Object of Users model class
     */
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

    /**
     * <p>Find user by username.
     * </p>
     *
     * @param userName unique username of user
     * @return Object of Users model class
     */
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

    /**
     * <p>Find user by email address.
     * </p>
     *
     * @param email unique email address of user
     * @return Object of Users model class
     */
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

    /**
     * <p>Checks whether username is already present
     * or not.This method helps in user registration
     * process to show availability of username.
     * </p>
     *
     * @param userName_prefix username which a new user fill during registration
     * @return String username either available one or already present
     */
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

    /**
     * <p>Auto create App User Account. When user logged in with
     * 3rd party OAuth provider like google ,facebook etc
     * </p>
     *
     * @param connection object of connection interface
     * @return Object of Users model class
     */
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
        users.setName(userProfile.getFirstName());
        this.entityManager.persist(users);
        // Create default Role
        List<String> roleNames = new ArrayList<String>();
        roleNames.add(Role.ROLE_USER);
        this.appRoleDAO.createRoleFor(users, roleNames);

        return users;
    }

    /**
     * <p>Creates a new account of the user when user registers
     * account with user registration form.
     * </p>
     *
     * @param appUserForm user registration form with data
     * @param roleNames list of roles a user have
     * @return Object of Users model class
     */
    public Users registerNewUserAccount(AppUserForm appUserForm, List<String> roleNames) {
        Users users = new Users();
        users.setUsername(appUserForm.getUserName());
        users.setEmail(appUserForm.getEmail());
        users.setName(appUserForm.getName());
        users.setEnabled(true);
        String encrytedPassword = passwordEncoder().encode(appUserForm.getPassword());
        users.setPassword(encrytedPassword);
        this.entityManager.persist(users);
        this.entityManager.flush();

        this.appRoleDAO.createRoleFor(users, roleNames);

        return users;
    }

    /**
     * <p>Return an instance of custom password encoder
     * to encode user password before storing in database.
     * </p>
     *
     * @return A new instance of CustomPasswordEncoder
     */
    private PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
}