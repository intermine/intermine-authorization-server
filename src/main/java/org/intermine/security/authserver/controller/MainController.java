package org.intermine.security.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.form.AppUserForm;
import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.AuthenticatedUser;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.AuthenticatedUserRepository;
import org.intermine.security.authserver.security.Encryption;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.intermine.security.authserver.utils.SecurityUtil;
import org.intermine.security.authserver.utils.WebUtils;
import org.intermine.security.authserver.validator.AppUserValidator;
import org.intermine.security.authserver.validator.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.*;

/**
 * Main controller contains mainly those endpoints which
 * anyone can access without having any specific role.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@Transactional
public class MainController {

    /**
     * used to query oauth_client_detail table.
     */
    @Autowired
    CustomClientDetailsService customClientDetailsService;

    /**
     * Used to query users database.
     */
    @Autowired
    private AppUserDAO appUserDAO;

    /**
     * The connection factory registry implements the ConnectionFactoryLocator
     * interface.Below is an object of that.
     */
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * Use to query userconnection table in database.
     */
    @Autowired
    private UsersConnectionRepository connectionRepository;

    /**
     * AppUserValidator is a validator of user signup form and
     * below is an object of that.
     */
    @Autowired
    private AppUserValidator appUserValidator;

    /**
     * Used to query oauth_access_token table in database.
     */
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    /**
     * String variable to redirect back to previous request.
     */
    private String oauthRedirectUri = null;

    /**
     * <p>Setting AppUSer validator to AppUserForm data on
     * initialization.
     * </p>
     *
     * @param dataBinder Binds app user form data with validator
     */
    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {

        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == AppUserForm.class) {
            dataBinder.setValidator(appUserValidator);
        }
    }

    /**
     * <p>Get Mapping for welcome page of authorization server.
     *  </p>
     *
     * @return homePage template
     */
    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        return "homePage";
    }

    /**
     * <p>Get Mapping for /profile path. This method redirect user
     * to user dashboard if it's a normal user of authorization
     * server otherwise redirect to admin page if it's an admin
     * user.
     *  </p>
     *
     * @param model An Instance of Model class
     * @param principal Current logged in user, can be obtained from SecurityContex too
     * @return redirect to admin or user dashboard
     */
    @RequestMapping(value = {"/profile"}, method = RequestMethod.GET)
    public String profile(Model model,Principal principal) {
        String userName =principal.getName();
        if(userName.equals("admin")){
            return "redirect:/admin";
        }
        return "redirect:/user/userInfo";
    }

    /**
     * <p>Get Mapping for /login path. This method returns loginPage
     * template. Storing session request cache so that if user clicks
     * on create new account in between oauth flow then go back to it
     * after successful registration.
     *  </p>
     *
     * @param model An Instance of Model class
     * @param request An Instance HttpServletRequest
     * @param response An Instance HttpServletResponse
     * @return loginPage template
     */
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        oauthRedirectUri = savedRequest.getRedirectUrl();
        return "loginPage";
    }

    /**
     * <p>Get Mapping for /signin path. This method redirect
     * user to login path.
     *  </p>
     *
     * @param model An Instance of Model class
     * @return redirects to login path
     */
    @RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
    public String signInPage(Model model) {
        return "redirect:/login";
    }

    /**
     * <p>Get Mapping for /signup path. This method returns signup
     * form template for user with blank clientForm instance.
     * </p>
     *
     * @param model An Instance of Model class
     * @param request Instance of webRequest
     * @return signupPage template
     */
    @RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
    public String signupPage(WebRequest request, Model model) {
        ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
                connectionRepository);
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        AppUserForm myForm = null;
        if (connection != null) {
            myForm = new AppUserForm(connection);
        } else {
            myForm = new AppUserForm();
        }
        model.addAttribute("myForm", myForm);
        return "signupPage";
    }

    /**
     * <p>Post Mapping for /signup path. This method first checks
     * the user form information with the help of validator and then
     * make use of bindingresult, which will give error if there
     * is any in form otherwise extract the details from signup form,
     * store them in database and logged in the user.
     * </p>
     *
     * @param request Instance of webRequest
     * @param model An Instance of Model class
     * @param response Instance of HrrpServletResponse
     *
     * @return signupPage template
     */
    @RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
    public String signupSave(WebRequest request, //
                             Model model, //
                             @ModelAttribute("myForm") @Validated AppUserForm appUserForm, //
                             BindingResult result, //
                             final RedirectAttributes redirectAttributes,
                             HttpServletResponse response) throws IOException {

        // Validation error.
        if (result.hasErrors()) {
            return "signupPage";
        }

        List<String> roleNames = new ArrayList<String>();
        roleNames.add(Role.ROLE_USER);

        Users registered = null;


        try {
            registered = appUserDAO.registerNewUserAccount(appUserForm, roleNames);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", "Error " + ex.getMessage());
            return "signupPage";
        }

        if (appUserForm.getSignInProvider() != null) {
            ProviderSignInUtils providerSignInUtils //
                    = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
            providerSignInUtils.doPostSignUp(registered.getUsername(), request);
        }

        SecurityUtil.logInUser(registered, roleNames);

//        return "redirect:/userInfo";
        response.sendRedirect(oauthRedirectUri);
        return null;
    }


    /**
     * <p>Get Mapping for /contact path. This method returns
     * contactPage template.
     * </p>
     *
     * @param model An Instance of Model class
     * @return contactPage template
     */
    @RequestMapping(value = {"/contact"}, method = RequestMethod.GET)
    public String contactPage(Model model) {
        return "contactPage";
    }

    /**
     * <p>Get Mapping for /isLoggedIn path. This endpoint
     * is used in cross-domain sso. Any Client can make request
     * on this endpoint which will first check that whether user
     * is already logged in the browser from which client is making
     * request and then also checks whether that user is already
     * authenticated with the client or not.
     * </p>
     *
     * @param model An Instance of Model class
     * @param request HttpServletRequest instance
     * @param user Current logged in user
     * @return true if both checks true otherwise false
     */
    @RequestMapping(value = "/isLoggedIn", method = RequestMethod.GET)
    @ResponseBody
    public boolean isLoggedIn(Model model, HttpServletRequest request, Principal user) {
        String clientId=request.getParameter("client");
        try{
            if(user!=null){
                AuthenticatedUser track = authenticatedUserRepository.findByUsernameAndClientId(user.getName(),clientId);
                return track != null;
            }
        }
        catch (NullPointerException ignored){
        }
        return false;

    }

}
