package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.utils.WebUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Custom Error controller contains endpoints to to map
 * different templates with different error status.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    /**
     * <p>Mapping for default /error path. Maps error template
     * with this path.
     * </p>
     *
     * @param request HttpServletRequest instance to get error status code.
     * @return error template according to status code.
     */
    @RequestMapping("")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

    /**
     * <p>Mapping for default /403 path. Maps 403Page template
     * with some custom message for access denied error.
     * </p>
     *
     * @param model Model to add attribute for template
     * @param principal Current logged in user
     * @return 403Page template with model attributes
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

    /**
     * <p>Returns error path.
     * </p>
     *
     * @return Default error path.
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
