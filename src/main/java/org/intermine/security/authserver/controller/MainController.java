package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.dao.AppUserDAO;
import org.intermine.security.authserver.form.AppUserForm;
import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.Role;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.intermine.security.authserver.utils.SecurityUtil;
import org.intermine.security.authserver.utils.WebUtils;
import org.intermine.security.authserver.validator.AppUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.*;

@Controller
@Transactional
public class MainController {

    @Autowired
    CustomClientDetailsService customClientDetailsService;

    @Autowired
    private AppUserDAO appUserDAO;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    private UsersConnectionRepository connectionRepository;

    @Autowired
    private AppUserValidator appUserValidator;

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

    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "adminPage";
    }


    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "userInfoPage";
    }


    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> currentUserName(Principal principal) {
        Users user= appUserDAO.findAppUserByUserName(principal.getName());
        Map<String, String> map = new LinkedHashMap<>();
        map.put("username",user.getUsername());
        map.put("email",user.getEmail());
        map.put("sub", String.valueOf(user.getUserId()));
        return map;
    }

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


    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Model model) {
        return "loginPage";
    }

    @RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
    public String signInPage(Model model) {
        return "redirect:/login";
    }

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

    @RequestMapping(value = { "/clientRegistration" }, method = RequestMethod.GET)
    public String clientRegistrationPage(WebRequest request, Model model) {
        ClientForm myForm=null;
        myForm = new ClientForm();
        model.addAttribute("myForm", myForm);
        return "clientRegistrationPage";
    }

    @RequestMapping(value = {"/clientRegistration"},method = RequestMethod.POST)
    public String clientSave(Model model,@ModelAttribute("myForm") ClientForm clientForm, Principal principal,
                             BindingResult result){
        if(result.hasErrors()){return "clientRegistrationPage";}

        try{
            OauthClientDetails oauthClientDetails=new OauthClientDetails();
            oauthClientDetails.setWebsiteUrl(clientForm.getWebsiteUrl());
            oauthClientDetails.setClientName(clientForm.getClientName());
            oauthClientDetails.setRegisteredRedirectUri(clientForm.getRegisteredRedirectUri());
            oauthClientDetails.setClientType(clientForm.getClientType());
            oauthClientDetails.setRegisteredBy(principal.getName());
            HashMap<String, String> clientInfo=customClientDetailsService.addCustomClientDetails(oauthClientDetails);
            model.addAttribute("clientId",clientInfo.get("client_id"));
            model.addAttribute("clientSecret",clientInfo.get("client_secret"));

        }
        catch (Exception ex){ex.printStackTrace();
            model.addAttribute("errorMessage", "Error " + ex.getMessage());
            return "clientRegistrationPage";}

        return "clientInfoPage";
    }


    @RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
    public String signupSave(WebRequest request, //
                             Model model, //
                             @ModelAttribute("myForm") @Validated AppUserForm appUserForm, //
                             BindingResult result, //
                             final RedirectAttributes redirectAttributes) {

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

        return "redirect:/userInfo";
    }
}
