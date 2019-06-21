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

@Repository
@Transactional
public class AppRoleDAO {

    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<String> getRoleNames(Integer userId) {
        //TODO Implement this function to return all the roles
        /*
        String sql = "Select role.name from role_user,role where user_id = :userId";
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
        */
        ArrayList<String> list = new ArrayList<String>();
        list.add("ROLE_USER");
        return list;
    }

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

