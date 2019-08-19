package org.intermine.security.authserver.config;

import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.repository.ClientDetailRepository;
import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * This class handles the http security and Cors(cross origin resource sharing) configurations.
 * Also, sets the custom properties in default methods of WebSecurityConfigurerAdapter.
 *
 * @author Rahul Yadav
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * An object of spring default UserDetailsService class.This object is used by
     * AuthenticationManagerBuilder class to create an authentication manager.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * An object of jpa repository to query oauth_client_details table in database.
     */
    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    /**
     * <p>Configures the setting globally for AuthenticationManagerBuilder, which is a
     * SecurityBuilder used to create an AuthenticationManager. Allows for easily
     * building in memory authentication, LDAP authentication, JDBC based authentication,
     * adding UserDetailsService, and adding AuthenticationProvider's.
     *  </p>
     *
     * @param auth An Instance of AuthenticationManagerBuilder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * <p>Returns the authenticationManager, whose job is to
     * establish a user's identity. An authentication manager is defined by the
     * AuthenticationManager interface.
     *  </p>
     *
     * @return authenticationManager An Instance of AuthenticationManager.
     */
    @Bean
    protected AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * <p>CustomPasswordEncoder is used to encode and match the encoded
     * credentials of client and user.
     * </p>
     *
     * @return A new instance of CustomPasswordEncoder class
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    /**
     * <p>Configures the setting for AuthenticationManagerBuilder, which is a
     * SecurityBuilder used to create an AuthenticationManager. Allows for easily
     * building in memory authentication, LDAP authentication, JDBC based authentication,
     * adding UserDetailsService, and adding AuthenticationProvider's.
     *  </p>
     *
     * @param auth An Instance of AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * <p>Spring default USerDetailsService used to load the user by username.
     * </p>
     *
     * @return An instance of spring default UserDetailsService class.
     */
    @Override
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    /**
     * <p>This method helps in configuring custom http security restrictions.
     * We can disable/enable default csrf security, allow cors and also can
     * add role based restrictions on our rest endpoints.
     * Also, we can set our own login process by setting login url,
     * defaultSuccessUrl, logout url and etc.
     *  </p>
     *
     * @param http An Instance of HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests().antMatchers("/", "/signup", "/login", "/logout", "/contact","/isLoggedIn").permitAll();
        http.authorizeRequests().antMatchers("/user/userInfo","/user/changePassword").access("hasRole('" + Role.ROLE_USER + "')");
        http.authorizeRequests().antMatchers("/profile").access("hasAnyRole('" + Role.ROLE_USER + "','" + Role.ROLE_ADMIN + "')");
        http.authorizeRequests().antMatchers("/admin","/client/verifyClient").access("hasRole('" + Role.ROLE_ADMIN + "')");
        http.authorizeRequests().antMatchers("/client/updateClient").access("hasRole('" + Role.ROLE_USER + "')");
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error/403");
        http.authorizeRequests()
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/user/userInfo")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password");
        http.authorizeRequests().and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
        http.apply(new SpringSocialConfigurer()).signupUrl("/signup");


    }

    /**
     * <p>This method is adding website url of all the registered clients in
     * allowed origins list so that every client can make a cross domain
     * request on this authorization server.
     * This helps in achieving cross domain SSo.
     * </p>
     *
     * @return An instance of UrlBasedCorsConfigurationSource class.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(iOauthClientDetails.findWebsiteUrls());
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
