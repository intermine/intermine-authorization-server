package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository to query oauth_access_token table
 * with the help of AuthenticatedUser model.
 *
 * @author Rahul Yadav
 *
 */
public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser,Integer> {
    /**
     * <p>Query the table and find unique row with username and clientId
     *  </p>
     *
     * @param username user to find
     * @param clientId is user is authenticated with this clientId or not
     * @return AuthenticatedUser model
     */
    AuthenticatedUser findByUsernameAndClientId(String username,String clientId);
}
