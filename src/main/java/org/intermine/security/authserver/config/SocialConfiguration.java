package org.intermine.security.authserver.config;


import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.service.ConnectionSignUpImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;


/**
 * SocialConfiguration is used to configurer 3rd party social oauth provider
 *like Google and others on this authorization server.
 *
 * @see <a href= "https://docs.spring.io/spring-social/docs/1.0.x/reference/html/connecting.html">How to configure other social OAuth providers</a>
 * @see <a href="https://docs.spring.io/spring-social/docs/current/reference/htmlsingle/">More about spring social</a>
 * @author Rahul Yadav
 *
 */
@Configuration
@EnableSocial
@PropertySource("classpath:social-cfg.properties")
public class SocialConfiguration implements SocialConfigurer {

    /**
     * A DataSource object is the preferred means of getting a connection.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Used to query users database.
     */
    @Autowired
    private AppUserDAO appUserDAO;

    /**
     * autoSignUp false will not redirect user to signup page on InterMine
     * authorization server after successful authentication with 3rd party
     * Oauth provider.
     */
    private boolean autoSignUp = false;

    /**
     * <p>This method add connection factory of google.
     * A ConnectionFactory abstraction encapsulates the construction of connections
     * that use a specific authorization protocol.
     *  </p>
     *
     * @param cfConfig An Instance of ConnectionFactoryConfigurer
     * @param env An environment variable to get google client id and secret.
     */
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        try {
            this.autoSignUp = Boolean.parseBoolean(env.getProperty("social.auto-signup"));
        } catch (Exception e) {
            this.autoSignUp = false;
        }


        GoogleConnectionFactory gfactory = new GoogleConnectionFactory(env.getProperty("google.client.id"), env.getProperty("google.client.secret"));
        gfactory.setScope(env.getProperty("google.scope"));
        cfConfig.addConnectionFactory(gfactory);


    }

    /**
     * <p>Returns user id source of authenticated user.
     *  </p>
     *
     * @return  A new instance of AuthenticationNameUserIdSource
     */
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    /**
     * <p>Used to get userconnectionrepository, which is a jdbc repository
     * to query on userconnection table. A userconnection table contains
     * entries of user who authenitcated by 3rd party OAuth provider like
     * google on Intermine authorization server.
     *  </p>
     *
     * @param connectionFactoryLocator An Instance of ConnectionFactoryLocator
     * @return UserConnectionRepository
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository usersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());

        if (autoSignUp) {
            ConnectionSignUp connectionSignUp = new ConnectionSignUpImpl(appUserDAO);
            usersConnectionRepository.setConnectionSignUp(connectionSignUp);
        } else {
            usersConnectionRepository.setConnectionSignUp(null);
        }
        return usersConnectionRepository;
    }

    /**
     * <p>Connect controller is a Spring MVC controller that coordinates
     * the connection flow between an application and service providers.
     * ConnectController takes care of redirecting the user to the service
     * provider for authorization and responding to the callback after
     * authorization.
     *  </p>
     *
     * @param connectionFactoryLocator An Instance of ConnectionFactoryLocator
     * @param connectionRepository An Instance of ConnectionRepository
     * @return A new instance of connectController.
     */
     @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

}

