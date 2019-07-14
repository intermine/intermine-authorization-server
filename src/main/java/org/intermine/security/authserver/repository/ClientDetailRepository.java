package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDetailRepository extends JpaRepository<OauthClientDetails, Integer> {
    Optional<OauthClientDetails> findByClientId(String clientId);
    OauthClientDetails findByClientName(String clientName);
    OauthClientDetails findByWebsiteUrl(String websiteUrl);
}
