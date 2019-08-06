package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.security.Encryption;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.intermine.security.authserver.utils.WebUtils;
import org.intermine.security.authserver.validator.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomClientDetailsService customClientDetailsService;

    @Autowired
    private ClientValidator clientValidator;

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
        ClientForm myForm = null;
        myForm = new ClientForm();
        model.addAttribute("myForm", myForm);
        model.addAttribute("map",map);
        model.addAttribute("secretList",secretList);
        model.addAttribute("clientList", clientList);

        return "userProfile";
    }

    @RequestMapping(value = {"/userInfo"}, method = RequestMethod.POST)
    public String clientSave(Model model,
                             @ModelAttribute("myForm") @Validated ClientForm clientForm,
                             BindingResult result,
                             Principal principal) {
        if (result.hasErrors()) {
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
}
