package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Jpa repository to query oauth_client_details table
 * with the help of OauthClientDetails model.
 *
 * @author Rahul Yadav
 *
 */
public interface ClientDetailRepository extends JpaRepository<OauthClientDetails, Integer> {
    /**
     * <p>Query the table and find unique client with clientId
     *  </p>
     *
     * @param clientId client to find
     * @return OauthClientDetails model
     */
    OauthClientDetails findByClientId(String clientId);

    /**
     * <p>Query the table and find unique client with clientName
     *  </p>
     *
     * @param clientName client to find
     * @return OauthClientDetails model
     */
    OauthClientDetails findByClientName(String clientName);

    /**
     * <p>Query the table and find unique client with registered website url
     *  </p>
     *
     * @param websiteUrl client website url
     * @return OauthClientDetails model
     */
    OauthClientDetails findByWebsiteUrl(String websiteUrl);

    /**
     * <p>Query the table and find all the clients registered by particular user
     *  </p>
     *
     * @param registeredBy who registered the client
     * @return List of OauthClientDetails registered by user
     */
    List<OauthClientDetails> findAllByRegisteredBy(String registeredBy);

    /**
     * <p>Query the table to get all the rows
     *  </p>
     *
     * @return list of all OauthClientDetails
     */
    List<OauthClientDetails> findAll();

    /**
     * <p>Query the table to get number client having bool
     * status. This is used to count the number of verified clients or
     * unverified clients.
     *  </p>
     *
     * @param bool find client with bool status
     * @return Long count number
     */
    Long countByStatus(boolean bool);

    /**
     * <p>Delete the client from table having clientName as in parameter.
     *  </p>
     *
     * @param clientName client to delete
     */
    void deleteByClientName(String clientName);

    /**
     * <p>Save or update the row in table.
     *  </p>
     */
    @Override
    <S extends OauthClientDetails> S save(S s);

    /**
     * <p>Query the table to get website url of all the registered
     * clients.
     *  </p>
     *
     * @return list of website urls
     */
    public static final String FIND_URLS = "SELECT client_website_url FROM oauth_client_details";
    @Query(value = FIND_URLS, nativeQuery = true)
    public List<String> findWebsiteUrls();
}
