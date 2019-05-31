package org.intermine.security.authserver.service;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.repository.ClientDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(CustomClientDetailsService.class);

    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        Optional<OauthClientDetails> oauthClientDetails = iOauthClientDetails.findByClientId(clientId);



        if (!oauthClientDetails.isPresent()) {
            throw new ClientRegistrationException("invalid_client");
        }

        OauthClientDetails client = oauthClientDetails.get();

		/*client.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList("password", "authorization_code",
				"refresh_token", "implicit")));
		client.setScope(new HashSet<>(Arrays.asList("scope_read", "scope_write", "trust")));*/

        String resourceIds = client.getResourceIds().stream().collect(Collectors.joining(","));
        String scopes = client.getScope().stream().collect(Collectors.joining(","));
        String grantTypes = client.getAuthorizedGrantTypes().stream().collect(Collectors.joining(","));

        Logger.debug("RESOURCE_ID {}, SCOPE {}, GRANT_TYPES {}", resourceIds, scopes, grantTypes);

        return new BaseClientDetails(client);
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        try {
            OauthClientDetails oauthClientDetail = new OauthClientDetails();
            setOrUpdateClientDetails(oauthClientDetail, clientDetails);
            oauthClientDetail.setClientId(clientDetails.getClientId());
            oauthClientDetail.setClientSecret(clientDetails.getClientSecret() != null ? passwordEncoder.encode(clientDetails.getClientSecret())
                    : null);
            iOauthClientDetails.save(oauthClientDetail);
            //  iOauthClientDetails.createApplication(oauthClientDetail);


        } catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
        }
    }

    private void setOrUpdateClientDetails(OauthClientDetails oauthClientDetail, ClientDetails clientDetails) {
        oauthClientDetail.setResourceIds(clientDetails.getResourceIds());
        oauthClientDetail.setScope(clientDetails.getScope());
        oauthClientDetail.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
        oauthClientDetail.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
        oauthClientDetail.setAuthorities(clientDetails.getAuthorities());
        oauthClientDetail.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
        oauthClientDetail.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
        //oauthClientDetail.setAdditionalInformation();
    }
}
