package org.intermine.security.authserver.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class for oauth_access_token table.
 *
 * @author Rahul Yadav
 *
 */
@JsonSerialize
@ToString
@Entity(name = "AuthenticatedUser")
@Table(name = "oauth_access_token")
public class AuthenticatedUser implements Serializable {

    /**
     * Construct an authenticatedUser.
     */
    public  AuthenticatedUser(){}

    /**
     *Auto generated row id of table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     *Token id of generated access_token
     */
    @Column(name = "token_id")
    private String tokenId;

    /**
     *Byte array data of access_token.
     */
    @Column(name = "token")
    private byte[] token;

    /**
     * Id of a unique user-client successful authentication.
     */
    @Column(name = "authentication_id")
    private String authenticationId;

    /**
     * The user for which the token is generated.
     */
    @Column(name = "user_name")
    private String username;

    /**
     * The client for which the user is authenticated
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * Byte data of authentication
     */
    @Column(name = "authentication")
    private byte[] authentication;

    /**
     * Refresh token to generate valid access token again
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * <p>Used to get unique token id.
     * </p>
     *
     * @return String unique identity of token
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * <p>Used to set token id for access token.
     * </p>
     *
     * @param tokenId unique tokenId of access_token
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * <p>Used to get access token.
     * </p>
     *
     * @return byte[] byte data of token
     */
    public byte[] getToken() {
        return token;
    }

    /**
     * <p>Used to set byte data of token.
     * </p>
     *
     * @param token byte data of access_token
     */
    public void setToken(byte[] token) {
        this.token = token;
    }

    /**
     * <p>Used to get unique user-client authentication id.
     * </p>
     *
     * @return String authentication id
     */
    public String getAuthenticationId() {
        return authenticationId;
    }

    /**
     * <p>Used to set authentication id for unique user-client pair.
     * </p>
     *
     * @param authenticationId authentication id of user-client
     */
    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    /**
     * <p>Used to get byte data of authentication.
     * </p>
     *
     * @return byte[] authentication
     */
    public byte[] getAuthentication() {
        return authentication;
    }

    /**
     * <p>Used to set byte data of authentication
     * </p>
     *
     * @param authentication authentication data in byte
     */
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    /**
     * <p>Used to get refresh token.
     * </p>
     *
     * @return String unique refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * <p>Used to set refresh token.
     * </p>
     *
     * @param refreshToken refresh token for token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * <p>Used to get row id .
     * </p>
     *
     * @return Integer unique row id of table
     */
    public Integer getId() {
        return id;
    }

    /**
     * <p>Used to set row id in table.
     * </p>
     *
     * @param id unique row id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * <p>Used to get username of user.
     * </p>
     *
     * @return String unique username
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Used to set username  of user.
     * </p>
     *
     * @param username username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Used to get id of client.
     * </p>
     *
     * @return String client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * <p>Used to set id of client.
     * </p>
     *
     * @param clientId unique identity of client
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
