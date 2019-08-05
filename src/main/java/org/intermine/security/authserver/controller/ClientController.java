package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.form.ClientForm;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.security.Encryption;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.intermine.security.authserver.validator.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/client")
@Transactional
public class ClientController {

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

    @RequestMapping(value = { "/clientRegistration" }, method = RequestMethod.GET)
    public String clientRegistrationPage(WebRequest request, Model model) {
        ClientForm myForm=null;
        myForm = new ClientForm();
        model.addAttribute("myForm", myForm);
        return "clientRegistrationPage";
    }

    @RequestMapping(value = {"/clientRegistration"},method = RequestMethod.POST)
    public String clientSave(Model model,
                             @ModelAttribute("myForm") @Validated ClientForm clientForm,
                             BindingResult result,
                             Principal principal){
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

    @RequestMapping(value = "/registeredClients", method = RequestMethod.GET)
    public String registeredClientsInfo(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        List<OauthClientDetails> clientList=customClientDetailsService.loadClientByUsername(userName);
        HashMap<OauthClientDetails, String> secretMap = new HashMap<>();
        for (OauthClientDetails element : clientList) {
            if(element.getClientSecret()!=null) {
                String encodedSecret = element.getClientSecret();
                try {
                    String decodedSecret= Encryption.DecryptAESCBCPCKS5Padding(encodedSecret);
                    secretMap.put(element,decodedSecret);
                } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("clientList", clientList);
        model.addAttribute("secretMap",secretMap);
        return "registeredClientsPage";
    }

    @RequestMapping(value={"/updateClient"},method=RequestMethod.POST,params = "update")
    public String clientUpdate(@RequestParam(value="registeredRedirectUri",required=false) String redirecturi, @RequestParam(value = "ClientName",required = false) String clientName){
        customClientDetailsService.updateClientRedirectUri(clientName,redirecturi);
        return "redirect:/registeredClients";
    }

    @RequestMapping(value={"/updateClient"},method=RequestMethod.POST,params = "delete")
    public String clientDelete(@RequestParam(value = "ClientName",required = false) String clientName){
        customClientDetailsService.deleteClient(clientName);
        return "redirect:/registeredClients";
    }
}
