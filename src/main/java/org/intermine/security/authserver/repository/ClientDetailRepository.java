package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientDetailRepository extends JpaRepository<OauthClientDetails, Integer> {
    Optional<OauthClientDetails> findByClientId(String clientId);
    OauthClientDetails findByClientName(String clientName);
    OauthClientDetails findByWebsiteUrl(String websiteUrl);
    List<OauthClientDetails> findAllByRegisteredBy(String registeredBy);
    void deleteByClientName(String clientName);
    @Override
    <S extends OauthClientDetails> S save(S s);

    public static final String FIND_URLS = "SELECT client_website_url FROM oauth_client_details";
    @Query(value = FIND_URLS, nativeQuery = true)
    public List<String> findWebsiteUrls();
}
