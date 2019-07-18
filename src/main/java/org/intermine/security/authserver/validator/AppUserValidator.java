package org.intermine.security.authserver.validator;


import org.apache.commons.validator.routines.EmailValidator;
import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.form.AppUserForm;
import org.intermine.security.authserver.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AppUserValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private AppUserDAO appUserDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AppUserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        AppUserForm form = (AppUserForm) target;

        if (errors.hasErrors()) {
            return;
        }

        if (!emailValidator.isValid(form.getEmail())) {

            errors.rejectValue("email", "", "Email is not valid");
            return;
        }

        Users userAccount = appUserDAO.findAppUserByUserName(form.getUserName());
        if (userAccount != null) {
            if (form.getUserId() == null) {
                errors.rejectValue("userName", "", "User name is not available");
                return;
            } else if (!form.getUserId().equals(userAccount.getUserId())) {
                errors.rejectValue("userName", "", "User name is not available");
                return;
            }
        }

        userAccount = appUserDAO.findByEmail(form.getEmail());
        if (userAccount != null) {
            if (form.getUserId() == null) {
                errors.rejectValue("email", "", "Email is not available");
                return;
            } else if (!form.getUserId().equals(userAccount.getUserId())) {
                errors.rejectValue("email", "", "Email is not available");
                return;
            }
        }
    }
}