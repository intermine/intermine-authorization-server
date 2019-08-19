package org.intermine.security.authserver.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JacksonArrayOrStringDeserializer;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * Model class for oauth_client_details table.
 *
 * @author Rahul Yadav
 *
 */
@JsonSerialize
@ToString
@Entity(name = "OauthClientDetails")
@Table(name = "oauth_client_details")
@SuppressWarnings("serial")
public class OauthClientDetails implements ClientDetails, Serializable {

    /**
     * Unique identifier for Serializable class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ObjectMapper provides functionality for reading and writing JSON.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     *Auto generated row id of table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * unique identifier of client.
     */
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    /**
     * Encrypted client credential
     */
    @Column(name = "client_secret", nullable = false, unique = true)
    private String clientSecret;

    /**
     * Name of the client given by its owner
     */
    @Column(name = "client_name", nullable = false, unique = true)
    private String clientName;

    /**
     * Genuine website of client register by its owner
     */
    @Column(name = "client_website_url", nullable = false, unique = true)
    private String websiteUrl;

    /**
     * Resources which a client can access
     */
    @Column(name = "resource_ids")
    private String resourceIds;

    /**
     * Information of user which a client want to
     * access like profile,email, openid etc
     */
    @Column(name = "scope")
    private String scope;

    /**
     * OAuth2.0 authorization grant types i.e
     * the way a client gets an access token from
     * auth server.
     */
    @Column(name = "authorized_grant_types", nullable = false)
    private String authorizedGrantTypes;

    /**
     * Urls on which a client wants to get
     * authorization code for user during
     * OAuth flow.
     */
    @Column(name = "web_server_redirect_uri")
    private String registeredRedirectUri;

    /**
     * Authorities a client have.
     */
    @Column(name = "authorities")
    private String authorities;

    /**
     * Validity of access token in seconds generated
     * for this client users.
     */
    @Column(name = "access_token_validity", nullable = false)
    private Integer accessTokenValiditySeconds;

    /**
     * Refresh token validity in seconds
     */
    @Column(name = "refresh_token_validity", nullable = false)
    private Integer refreshTokenValiditySeconds;

    /**
     * Auto approve a client for the named
     * scopes.
     */
    @Column(name = "autoapprove", nullable = false)
    private String autoApproveScope;

    /**
     * Additional information, if any.
     */
    @Column(name = "additional_information")
    private String additionalInformation;

    /**
     * Type of client i.e android, website etc.
     */
    @Column(name = "client_type")
    private String clientType;

    /**
     * Client owner.
     */
    @Column(name = "registered_by")
    private String registeredBy;

    /**
     * Verification status i.e verified by admin
     * or not.
     */
    @Column(name= "status")
    private boolean status;

    /**
     * Construct an OauthClientDetails instance.
     */
    public OauthClientDetails() {}

    /**
     * Construct an OauthClientDetails instance.
     */
    public OauthClientDetails(OauthClientDetails oauthClientDetails) {}

    /**
     * <p>Used to set additional information of
     * client.
     * </p>
     *
     * @param additionalInformation information to add
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     * <p>Used to check autoapprove scope status
     * of the client.
     * </p>
     *
     * @return Set of string scopes which are auto approve by client
     */
    private Set<String> getAutoApproveScope() {
        return StringUtils.commaDelimitedListToSet(this.autoApproveScope);
    }

    /**
     * <p>Used to set auto approve value for client.
     * </p>
     *
     * @param autoApproveScope set of scopes to auto approve a client for
     */
    public void setAutoApproveScope(Set<String> autoApproveScope) {
        this.autoApproveScope = StringUtils.collectionToCommaDelimitedString(autoApproveScope);
    }

    /**
     * <p>Used to get id of client.
     * </p>
     *
     * @return String client id
     */
    @Override
    public String getClientId() {
        return this.clientId;
    }

    /**
     * <p>Used to set the client Id .
     * </p>
     *
     * @param clientId unique identifier of client
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * <p>Used to get resources which a client
     * have access for.
     * </p>
     *
     * @return Set of String resources
     */
    @Override
    public Set<String> getResourceIds() {
        if (StringUtils.isEmpty(this.resourceIds)) {
            return new HashSet<>();
        } else {
            return StringUtils.commaDelimitedListToSet(this.resourceIds);
        }
    }

    /**
     * <p>Used to set the resource id which
     * a client can access.
     * </p>
     *
     * @param resourceIds set of resources
     */
    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = StringUtils.collectionToCommaDelimitedString(resourceIds);
    }

    /**
     * <p>Used to check if secret credential is
     * required for the client or not.
     * </p>
     *
     * @return boolean true if required else false
     */
    @Override
    public boolean isSecretRequired() {
        return !StringUtils.isEmpty(this.clientSecret);
    }

    /**
     * <p>Used to get secret credential of
     * the client.
     * </p>
     *
     * @return String encrypted client secret credential
     */
    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    /**
     * <p>Used to set client secret credential.
     * </p>
     *
     * @param clientSecret encrypted secret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * <p>Used to get name of the client.
     * </p>
     *
     * @return String client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * <p>Used to set name of the client.
     * </p>
     *
     * @param clientName client name
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * <p>Used to get website url of the client.
     * </p>
     *
     * @return String client website url
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * <p>Used to website url of the client.
     * </p>
     *
     * @param websiteUrl url of client
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @Override
    public boolean isScoped() {
        return this.getScope().size() > 0;
    }

    /**
     * <p>Used to get scopes a client want to access.
     * </p>
     *
     * @return Set of string scopes
     */
    @Override
    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(this.scope);
    }

    /**
     * <p>Used to set scopes for the client.
     * i.e user information which a client can access.
     * </p>
     *
     * @param scope set of scopes
     */
    public void setScope(Set<String> scope) {
        this.scope = StringUtils.collectionToCommaDelimitedString(scope);
    }

    /**
     * <p>Used to get authorized grant types a client
     * have.
     * </p>
     *
     * @return Set of string authorized grant types
     */
    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
    }

    /**
     * <p>Used to set authorization grant types
     * for client.
     * </p>
     *
     * @param authorizedGrantType set of authorization grant types
     */
    public void setAuthorizedGrantTypes(Set<String> authorizedGrantType) {
        this.authorizedGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizedGrantType);
    }

    /**
     * <p>Used to get redirect uri's of client.
     * </p>
     *
     * @return Set of string redirect uri's
     */
    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
    }

    /**
     * <p>Used to set redirect uri's.
     * </p>
     *
     * @param registeredRedirectUriList set of redirect uri's
     */
    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

    /**
     * <p>Used to get authorities a client have.
     * </p>
     *
     * @return collection granted authorities
     */
    @org.codehaus.jackson.annotate.JsonProperty("authorities")
    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("authorities")
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<String> set = StringUtils.commaDelimitedListToSet(String.valueOf(this.authorities));
        Set<GrantedAuthority> result = new HashSet<>();
        set.forEach(authority -> result.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authority;
            }
        }));
        return result;
    }

    /**
     * <p>Used to set authority a client have.
     * </p>
     *
     * @param authorities authority a client have.
     */
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    /**
     * <p>Used to remaining validity of access token of client.
     * </p>
     *
     * @return integer remaining validity of token in seconds.
     */
    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    /**
     * <p>Used to set the validity of access token
     * generated for the client users.
     * </p>
     *
     * @param accessTokenValiditySeconds validity of token in seconds
     */
    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    /**
     * <p>Used to get remaining refresh token validity of client.
     * </p>
     *
     * @return Integer remaining validity of token in seconds.
     */
    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    /**
     * <p>Used to set the validity of refresh token.
     * </p>
     *
     * @param refreshTokenValiditySeconds validity of token in seconds
     */
    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    /**
     * <p>Used to get auto approve status of client.
     * </p>
     *
     * @param scope scope to check status for
     * @return boolean true if is auto approve else false
     */
    @Override
    public boolean isAutoApprove(String scope) {
        if (this.autoApproveScope == null) {
            return false;
        } else {
            Iterator scopeIterator = this.getAutoApproveScope().iterator();
            String auto;
            do {
                if (!scopeIterator.hasNext()) {
                    return false;
                }
                auto = (String) scopeIterator.next();
            } while (!auto.equals("true") && !scope.matches(auto));
            return true;
        }
    }

    /**
     * <p>Used to additional information of the client.
     * </p>
     *
     * @return Map additional information
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAdditionalInformation() {
        try {
            return mapper.readValue(this.additionalInformation, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    /**
     * <p>Used to set additionalInformation of the client.
     * </p>
     *
     * @param additionalInformation mapped information
     */
    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        try {
            this.additionalInformation = mapper.writeValueAsString(additionalInformation);
        } catch (IOException e) {
            this.additionalInformation = "";
        }
    }

    /**
     * <p>Used to get type of the client.
     * </p>
     *
     * @return String client type
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * <p>Used to set type of the client.
     * </p>
     *
     * @param clientType type of the client
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * <p>Used to get owner of client.
     * </p>
     *
     * @return String who registered the client
     */
    public String getRegisteredBy() {
        return registeredBy;
    }

    /**
     * <p>Used to set owner of the client.
     * </p>
     *
     * @param registeredBy client owner who registered the client
     */
    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    /**
     * <p>Used to check verification status
     * of client.
     * </p>
     *
     * @return boolean true if verified by admin else false
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * <p>Used to set verification status for the client
     * </p>
     *
     * @param status verification status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
}
