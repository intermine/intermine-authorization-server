package org.intermine.security.authserver.form;


import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Model class of Client Registration form.
 *
 * @author Rahul Yadav
 *
 */
public class ClientForm {

    /**
     * what user want to call a client
     */
    private String clientName;

    /**
     * Genuine website url of client
     */
    private String websiteUrl;

    /**
     * redirectUri of client
     */
    private String registeredRedirectUri;

    /**
     * type of client- app/website or other
     */
    private String clientType;

    /**
     * Construct a blank client registration form.
     */
    public ClientForm(){}

    /**
     * <p>Used to get type of client.
     * </p>
     *
     * @return clientType
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * <p>Used to set type of client.
     * </p>
     *
     * @param  clientType type of client
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * <p>Used to get website url of client.
     * </p>
     *
     * @return websiteUrl
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * <p>Used to set website url of client.
     * </p>
     *
     * @param  websiteUrl url of client website
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * <p>Used to get set registered redirect url's of client.
     * </p>
     *
     * @return set of redirect uri's
     */
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
    }

    /**
     * <p>Used to set redirectUri's of client.
     * </p>
     *
     * @param registeredRedirectUriList set of redirectUri's
     */
    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

    /**
     * <p>Used to get name of the client.
     * </p>
     *
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * <p>Used to set name of the client.
     * </p>
     *
     * @param clientName name to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
