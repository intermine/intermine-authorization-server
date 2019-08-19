package org.intermine.security.authserver.config;

import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.intermine.security.authserver.security.CustomTokenConverter;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * AuthorizationServerConfiguration overrides methods of AuthorizationServerConfigurer
 * interface to provide custom functionality for IM authorization server.
 *
 * Please see the {@link org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer}
 * class for true identity
 * @author Rahul Yadav
 *
 */
@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer{

    /**
     * <p>CustomPasswordEncoder is used to encode and match the encoded
     * credentials of client and user.
     * </p>
     *
     * @return A new instance of CustomPasswordEncoder class
     */
    private PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    /**
     * A DataSource object is the preferred means of getting a connection.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * An Authentication object created by spring authentication filter is  used
     * to call the authenticate method in the AuthenticationManager interface.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * <p>clientDetailsService is used provide the functionality
     * of our customClientDetailServices.
     * </p>
     *
     * @return A new instance of CustomClientDetailsService which having current
     * datasource connection and password encoder.
     */
    @Autowired
    public ClientDetailsService clientDetailsService() {

        CustomClientDetailsService client = new CustomClientDetailsService(this.dataSource);
        client.setPasswordEncoder(this.passwordEncoder());
        return client;
    }

    /**
     * <p>By default spring uses inMemory to store the tokens where jdbcTokenStore
     * is used to store tokens in database itself.
     * </p>
     *
     * @return A new instance of JdbcTokenStore with current datasource connection.
     */
    @Bean
    TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * <p>Spring Security OAuth exposes two endpoints for checking tokens
     * (/oauth/check_token and /oauth/token_key).
     * These endpoints are not exposed by default (have access "denyAll()").
     * To verify the tokens with this endpoint this config is necessary.
     * </p>
     *
     * @param security An Instance of AuthorizationServerSecurityConfigurer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
        security.tokenKeyAccess("permitAll()")
                .passwordEncoder(this.passwordEncoder())
                .allowFormAuthenticationForClients();
    }

    /**
     * <p>Configuration to set our CustomClientDetailService instance with
     * Spring default ClientDetailsServiceConfigurer.
     * </p>
     *
     * @param configurer An Instance of ClientDetailsServiceConfigurer
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.withClientDetails(this.clientDetailsService());
    }

    /**
     * <p>Configuration used to update default endpoints of spring AuthorizationServerEndpointsConfigurer
     * with our custom endpoints.
     * </p>
     *
     * @param endpoints An Instance of AuthorizationServerEndpointsConfigurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(jdbcTokenStore())
        .accessTokenConverter(this.accessTokenConverter());
    }

    /**
     * Pass Key for our key value pair file (.p12)
     */
    private final static String PASS_KEY = "Intermine@123";

    /**
     * <p>accessTokenConverter is used to set the path of Key pair and
     * its PASS KEY to our customTokenConverter
     * </p>
     *
     * @return converter An Instance of our CustomTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        CustomTokenConverter converter = new CustomTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("certificate/1.p12"),
                PASS_KEY.toCharArray())
                .getKeyPair("1");
        converter.setKeyPair(keyPair);
        return converter;
    }

}
