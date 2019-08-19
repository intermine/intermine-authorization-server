package org.intermine.security.authserver.dao;


import org.intermine.security.authserver.model.UserConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Data access object class having access to database and
 * contains logic to access data from userconnection
 * table.
 *
 * @author Rahul Yadav
 *
 */
@Repository
@Transactional
public class UserConnectionDAO {

    /**
     * Used to read, delete and write an entity.
     * An object referenced by an entity is managed
     * by entity manager.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * <p>Used to get user details from userconnection table
     * using unique provider id.
     * </p>
     *
     * @param userProviderId provider i
     * @return Object of UserConnection model class
     */
    public UserConnection findUserConnectionByUserProviderId(String userProviderId) {
        try {
            String sql = "Select e from " + UserConnection.class.getName() + " e " //
                    + " Where e.userProviderId = :userProviderId ";

            Query query = entityManager.createQuery(sql, UserConnection.class);
            query.setParameter("userProviderId", userProviderId);

            @SuppressWarnings("unchecked")
            List<UserConnection> list = query.getResultList();

            return list.isEmpty() ? null : list.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }
}

