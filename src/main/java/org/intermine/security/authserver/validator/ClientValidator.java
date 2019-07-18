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

@Component
public class ClientValidator implements Validator {

    private UrlValidator websiteValidator=UrlValidator.getInstance();

    @Autowired
    private CustomClientDetailsService customClientDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ClientForm.class;
    }


    @Override
    public void validate(Object target, Errors errors) {
        ClientForm form = (ClientForm) target;

        if (errors.hasErrors()) {
            return;
        }
        if (!websiteValidator.isValid(form.getWebsiteUrl())) {

            errors.rejectValue("websiteUrl", "", "Url is not valid");
            return;
        }

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
