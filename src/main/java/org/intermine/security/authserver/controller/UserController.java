package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.UserDetailRepository;
import org.intermine.security.authserver.security.CustomPasswordEncoder;
import org.intermine.security.authserver.security.Encryption;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.intermine.security.authserver.service.SocialUserDetailsImpl;
import org.intermine.security.authserver.utils.WebUtils;
import org.intermine.security.authserver.validator.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User controller contains endpoint for userProfile
 * template.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * used to query oauth_client_detail table.
     */
    @Autowired
    CustomClientDetailsService customClientDetailsService;

    /**
     * validator object for client registration form.
     */
    @Autowired
    private ClientValidator clientValidator;

    /**
     * An object of jpa repository to query users table in database.
     */
    @Autowired
    UserDetailRepository userDetailRepository;

    /**
     * <p>Setting client validator to clientform data on
     * initialization.
     * </p>
     *
     * @param dataBinder Binds client form data with validator
     */
    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {

        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == ClientForm.class) {
            dataBinder.setValidator(clientValidator);
        }
    }

    /**
     * <p>Get Mapping for /userInfo path. This method returns
     * userProfile template with the required data in
     * model attributes to render on template.
     *  </p>
     *
     * @param model An Instance of Model class
     * @param principal Current logged in user, can be obtained from SecurityContex too
     * @return returns userProfile template
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        List<OauthClientDetails> clientList = null;
        List<String> secretList = new ArrayList<>();
        HashMap<OauthClientDetails, String> map = new HashMap<>();
        clientList = customClientDetailsService.loadClientByUsername(userName);
        for (OauthClientDetails element : clientList) {
            if(element.getClientSecret()!=null) {
                String encodedSecret = element.getClientSecret();
                try {
                    String decodedSecret= Encryption.DecryptAESCBCPCKS5Padding(encodedSecret);
                    secretList.add(decodedSecret);
                    map.put(element,decodedSecret);
                } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!model.containsAttribute("myForm")){
            ClientForm myForm = null;
            myForm = new ClientForm();
            model.addAttribute("myForm", myForm);
        }
        model.addAttribute("username",userName);
        model.addAttribute("map",map);
        model.addAttribute("secretList",secretList);
        model.addAttribute("clientList", clientList);

        return "userProfile";
    }

    /**
     * <p>Post Mapping for /userInfo path. This method
     * registers a new client in database if everything
     * is right otherwise return userProfile page again
     * with error attribute in model.
     *  </p>
     *
     * @param model An Instance of Model class
     * @param principal Current logged In  user
     * @return userProfile template
     */
    @RequestMapping(value = {"/userInfo"}, method = RequestMethod.POST)
    public String clientSave(Model model,
                             @ModelAttribute("myForm") @Validated ClientForm clientForm,
                             BindingResult result,
                             Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("clientRegistrationError",true);
            userInfo(model,principal);
            return "userProfile";
        }

        try {
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            oauthClientDetails.setWebsiteUrl(clientForm.getWebsiteUrl());
            oauthClientDetails.setClientName(clientForm.getClientName());
            oauthClientDetails.setRegisteredRedirectUri(clientForm.getRegisteredRedirectUri());
            oauthClientDetails.setClientType(clientForm.getClientType());
            oauthClientDetails.setRegisteredBy(principal.getName());
            HashMap<String, String> clientInfo = customClientDetailsService.addCustomClientDetails(oauthClientDetails);
            model.addAttribute("clientId", clientInfo.get("client_id"));
            model.addAttribute("clientSecret", clientInfo.get("client_secret"));

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", "Error " + ex.getMessage());
            return "userProfile";
        }

        model.addAttribute("clientRegistered",true);
        return "userProfile";
    }

    /**
     * <p>Get Mapping for /changePassword path. This method returns
     * changePassword template.
     *  </p>
     *
     * @param model An Instance of Model class
     * @return returns changePassword template
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String getChangePasswordForm(Model model){

        return "changePassword";
    }

    /**
     * <p>Post Mapping for /changePassword path. This method
     * updates current logged in user password with the new one.
     * First checks whether user enters correct current password
     * or not if it is correct then update it in database.
     *  </p>
     *
     * @param currentPass Current password enter by user in changePassword form
     * @param newPass New password to be updated
     * @param model An Instance of Model class
     * @param principal Current logged In  user
     * @return redirect user back to user dashboard if successful otherwise
     * returns back to changePassword template if have any error occurred
     */
    @RequestMapping(value = {"/changePassword"}, method = RequestMethod.POST)
    public String changePassword(@RequestParam(value="currentpassword",required=true) String currentPass, @RequestParam(value = "newpassword",required = false) String newPass,
                                 Principal principal, Model model, HttpServletRequest request, RedirectAttributes redirAttrs){
        if(currentPass.equals(newPass)){
            model.addAttribute("errorMessage", "Please enter different password.");
            return "changePassword";
        }
        SocialUserDetailsImpl socialUserDetails=((SocialUserDetailsImpl)((UsernamePasswordAuthenticationToken)principal).getPrincipal());
        String password=socialUserDetails.getPassword();
        String username=principal.getName();
        CustomPasswordEncoder passwordEncoder=new CustomPasswordEncoder();
        boolean result = passwordEncoder.matches(currentPass,password);
        if(!result){
            model.addAttribute("errorMessage", "Wrong password");
            return "changePassword";
        }
        userDetailRepository.updatePassword(passwordEncoder.encode(newPass),username);
        redirAttrs.addFlashAttribute("changePasswordMessage", "success");
        return "redirect:/user/userInfo";
    }
}
