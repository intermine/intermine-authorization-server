package org.intermine.security.authserver.form;


import org.springframework.util.StringUtils;

import java.util.Set;

public class ClientForm {
    private String clientName;
    private String websiteUrl;
    private String registeredRedirectUri;
    private String clientType;

    public ClientForm(){

    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
