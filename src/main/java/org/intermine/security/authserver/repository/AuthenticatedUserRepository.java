package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser,Integer> {
    AuthenticatedUser findByUsernameAndClientId(String username,String clientId);
}
