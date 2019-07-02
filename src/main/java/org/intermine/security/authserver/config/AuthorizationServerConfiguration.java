package org.intermine.security.authserver.config;

import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import javax.sql.DataSource;

@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer{

    private PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public ClientDetailsService clientDetailsService() {

        CustomClientDetailsService client = new CustomClientDetailsService(this.dataSource);
        client.setPasswordEncoder(this.passwordEncoder());
        return client;
    }

    @Bean
    TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
        security.tokenKeyAccess("permitAll()")
                .passwordEncoder(this.passwordEncoder())
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.withClientDetails(this.clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(jdbcTokenStore());
    }

}
