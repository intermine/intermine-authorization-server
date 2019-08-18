package org.intermine.security.authserver.dao;


import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.model.UserRole;
import org.intermine.security.authserver.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object class having access to database and
 * contains logic to access data from role_user table.
 *
 * @author Rahul Yadav
 *
 */
@Repository
@Transactional
public class AppRoleDAO {

    /**
     * Used to read, delete and write an entity.
     * An object referenced by an entity is managed
     * by entity manager.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * <p>Used to get Role by roleName.
     * </p>
     *
     * @param roleName name of role to fetch
     * @return Object of Role model
     */
    public Role findAppRoleByName(String roleName) {
        try {
            String sql = "Select e from " + Role.class.getName() + " e "
                    + " where e.name = :roleName ";

            Query query = this.entityManager.createQuery(sql, Role.class);
            query.setParameter("roleName", roleName);
            return (Role) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * <p>Set roles of a user.
     * </p>
     *
     * @param roleNames list of roles which a user have
     * @param users instance of Users model class
     */
    public void createRoleFor(Users users, List<String> roleNames) {
        //
        for (String roleName : roleNames) {
            Role role = this.findAppRoleByName(roleName);
            if (role == null) {
                role = new Role();
                role.setRoleName(Role.ROLE_USER);
                this.entityManager.persist(role);
                this.entityManager.flush();
            }
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUsers(users);
            this.entityManager.persist(userRole);
            this.entityManager.flush();
        }
    }


}

