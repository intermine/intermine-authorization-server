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

/**
 * AppUserValidator is used to validate user registration
 * form data.
 *
 * @author Rahul Yadav
 *
 */
@Component
public class AppUserValidator implements Validator {

    /**
     * Email validator to validate user email address.
     */
    private EmailValidator emailValidator = EmailValidator.getInstance();

    /**
     * Used to query users database.
     */
    @Autowired
    private AppUserDAO appUserDAO;

    /**
     * <p>Checks current class target is of AppUserForm or not.
     *  </p>
     *
     * @return boolean true if equals
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AppUserForm.class;
    }

    /**
     * <p>Validate form data from target with some custom checks.
     * Checks whether user is already registered or not ,username
     * is already registered by another user or not, Email is available
     * and in correct format or not.
     *  </p>
     *
     * @param target User registration form data
     * @param errors rejects if error occurred in validation
     */
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