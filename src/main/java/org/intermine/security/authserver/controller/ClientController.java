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

    @RequestMapping(value = {"/verifyClient"}, method = RequestMethod.POST, params = "verify")
    public String clientVerify(@RequestParam(value = "clientName", required = false) String clientName) throws NoSuchAlgorithmException {
        customClientDetailsService.verifyClient(clientName);
        return "redirect:/admin";
    }
}
