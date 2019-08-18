package org.intermine.security.authserver.validator;


import org.apache.commons.validator.routines.UrlValidator;
import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * ClientValidator is used to validate client registration
 * form data.
 *
 * @author Rahul Yadav
 *
 */
@Component
public class ClientValidator implements Validator {

    /**
     * Url validator to validate client website url.
     */
    private UrlValidator websiteValidator=UrlValidator.getInstance();

    /**
     * used to query oauth_client_detail table.
     */
    @Autowired
    private CustomClientDetailsService customClientDetailsService;

    /**
     * <p>Checks current class target is of clientForm or not.
     *  </p>
     *
     * @return boolean true if equals
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ClientForm.class;
    }

    /**
     * <p>Validate form data from target with some custom checks.
     * Checks whether client is already registered or not ,website url
     * is already registered by another user or not, is website url
     * is in correct format or not.
     *  </p>
     *
     * @param target Client registration form data
     * @param errors rejects if error occurred in validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        ClientForm form = (ClientForm) target;

        if (errors.hasErrors()) {
            return;
        }
        if(!form.getWebsiteUrl().startsWith("http://")){
            errors.rejectValue("websiteUrl", "","Url is not valid");
        }
//        if (!websiteValidator.isValid(form.getWebsiteUrl())) {
//
//            errors.rejectValue("websiteUrl", "", "Url is not valid");
//            return;
//        }

        OauthClientDetails oauthClientDetails = customClientDetailsService.loadClientByClientName(form.getClientName());
        if (oauthClientDetails != null) {
            if (form.getClientName() == null) {
                errors.rejectValue("clientName", "", "Client Name is not available");
                return;
            } else if (form.getClientName().equals(oauthClientDetails.getClientName())) {
                errors.rejectValue("clientName", "", "Client Name is not available");
                return;
            }
        }
        oauthClientDetails = customClientDetailsService.loadClientByWebsiteUrl(form.getWebsiteUrl());
        if (oauthClientDetails != null) {
            if (form.getWebsiteUrl() == null) {
                errors.rejectValue("websiteUrl", "", "Website Url is not available");
                return;
            } else if (form.getWebsiteUrl().equals(oauthClientDetails.getWebsiteUrl())) {
                errors.rejectValue("websiteUrl", "", "This Website is already registered");
                return;
            }
        }
    }
}
