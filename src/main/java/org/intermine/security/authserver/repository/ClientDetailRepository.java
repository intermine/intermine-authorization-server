package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientDetailRepository extends JpaRepository<OauthClientDetails, Integer> {
    OauthClientDetails findByClientId(String clientId);
    OauthClientDetails findByClientName(String clientName);
    OauthClientDetails findByWebsiteUrl(String websiteUrl);
    List<OauthClientDetails> findAllByRegisteredBy(String registeredBy);
    List<OauthClientDetails> findAll();
    Long countByStatus(boolean bool);
    void deleteByClientName(String clientName);
    @Override
    <S extends OauthClientDetails> S save(S s);
}
