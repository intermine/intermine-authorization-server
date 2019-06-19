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

@JsonSerialize
@ToString
@Entity(name = "OauthClientDetails")
@Table(name = "oauth_client_details")
@SuppressWarnings("serial")
public class OauthClientDetails implements ClientDetails, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper mapper = new ObjectMapper();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;
    @Column(name = "client_secret", nullable = false, unique = true)
    private String clientSecret;
    @Column(name = "client_name", nullable = false, unique = true)
    private String clientName;
    @Column(name = "client_website_url", nullable = false, unique = true)
    private String websiteUrl;
    @Column(name = "resource_ids")
    private String resourceIds;
    @Column(name = "scope")
    private String scope;
    @Column(name = "authorized_grant_types", nullable = false)
    private String authorizedGrantTypes;
    @Column(name = "web_server_redirect_uri")
    private String registeredRedirectUri;
    @Column(name = "authorities")
    private String authorities;
    @Column(name = "access_token_validity", nullable = false)
    private Integer accessTokenValiditySeconds;
    @Column(name = "refresh_token_validity", nullable = false)
    private Integer refreshTokenValiditySeconds;
    @Column(name = "autoapprove", nullable = false)
    private String autoApproveScope;
    @Column(name = "additional_information")
    private String additionalInformation;
    @Column(name = "client_type")
    private String clientType;
    @Column(name = "registered_by")
    private String registeredBy;

    public OauthClientDetails() {

    }

    public OauthClientDetails(OauthClientDetails oauthClientDetails) {
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    private Set<String> getAutoApproveScope() {
        return StringUtils.commaDelimitedListToSet(this.autoApproveScope);
    }

    public void setAutoApproveScope(Set<String> autoApproveScope) {
        this.autoApproveScope = StringUtils.collectionToCommaDelimitedString(autoApproveScope);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        if (StringUtils.isEmpty(this.resourceIds)) {
            return new HashSet<>();
        } else {
            return StringUtils.commaDelimitedListToSet(this.resourceIds);
        }
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = StringUtils.collectionToCommaDelimitedString(resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return !StringUtils.isEmpty(this.clientSecret);
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @Override
    public boolean isScoped() {
        return this.getScope().size() > 0;
    }

    @Override
    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(this.scope);
    }

    public void setScope(Set<String> scope) {
        this.scope = StringUtils.collectionToCommaDelimitedString(scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantType) {
        this.authorizedGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizedGrantType);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

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

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

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

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAdditionalInformation() {
        try {
            return mapper.readValue(this.additionalInformation, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        try {
            this.additionalInformation = mapper.writeValueAsString(additionalInformation);
        } catch (IOException e) {
            this.additionalInformation = "";
        }
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }
}
