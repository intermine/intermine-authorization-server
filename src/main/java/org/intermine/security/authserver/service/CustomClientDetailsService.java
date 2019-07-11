package org.intermine.security.authserver.service;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.repository.ClientDetailRepository;
import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.intermine.security.authserver.security.Encryption;
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
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.crypto.keygen.KeyGenerators.secureRandom;

@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(CustomClientDetailsService.class);

    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    private PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

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

    public HashMap<String, String> addCustomClientDetails(OauthClientDetails clientDetails) throws ClientAlreadyExistsException, NoSuchAlgorithmException {
        OauthClientDetails oauthClientDetail = new OauthClientDetails();
        String currentClientId=Encryption.SHA1(secureRandom(16).generateKey())+".apps.intermine.com";
        oauthClientDetail.setClientId(currentClientId);
        String currentClientSecret=Encryption.SHA1(secureRandom(16).generateKey());
        oauthClientDetail.setClientSecret(passwordEncoder().encode(currentClientSecret));
        oauthClientDetail.setClientName(clientDetails.getClientName());
        oauthClientDetail.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
        oauthClientDetail.setWebsiteUrl(clientDetails.getWebsiteUrl());
        oauthClientDetail.setAccessTokenValiditySeconds(3600);
        oauthClientDetail.setRefreshTokenValiditySeconds(10000);
        oauthClientDetail.setRegisteredBy(clientDetails.getRegisteredBy());
        oauthClientDetail.setClientType(clientDetails.getClientType());
        oauthClientDetail.setScope(new HashSet<String>(Arrays.asList("openid", "profile","email")));
        oauthClientDetail.setAuthorizedGrantTypes(new HashSet<String>(Arrays.asList("authorization_code","password","refresh_token","implicit")));
        iOauthClientDetails.save(oauthClientDetail);
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", currentClientId);
        map.put("client_secret", currentClientSecret);
        return map ;
    }
}
