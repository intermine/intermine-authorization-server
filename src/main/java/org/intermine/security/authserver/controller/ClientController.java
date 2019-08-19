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

/**
 * Client controller contains endpoints to make changes in
 * oauth_client_detail table.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@RequestMapping("/client")
@Transactional
public class ClientController {

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
     * <p>Post mapping to update registeredRedirectUri of client.
     * </p>
     *
     * @param redirecturi modified redirect uri to update
     * @param clientName client to update for.
     * @return Redirect back to UserInfo page
     */
    @RequestMapping(value={"/updateClient"},method=RequestMethod.POST,params = "update")
    public String clientUpdate(@RequestParam(value="registeredRedirectUri",required=false) String redirecturi, @RequestParam(value = "ClientName",required = false) String clientName){
        customClientDetailsService.updateClientRedirectUri(clientName,redirecturi);
        return "redirect:/user/userInfo";
    }

    /**
     * <p>Post mapping to delete registered client from database.
     * </p>
     *
     * @param clientName client to delete.
     * @return Redirect back to UserInfo page
     */
    @RequestMapping(value={"/updateClient"},method=RequestMethod.POST,params = "delete")
    public String clientDelete(@RequestParam(value = "ClientName",required = false) String clientName){
        customClientDetailsService.deleteClient(clientName);
        return "redirect:/user/userInfo";
    }

    /**
     * <p>Post mapping to verify client. This endpoint can only be access
     * by Admin.
     * </p>
     *
     * @param clientName client to verify.
     * @return Redirect back to UserInfo page
     */
    @RequestMapping(value = {"/verifyClient"}, method = RequestMethod.POST, params = "verify")
    public String clientVerify(@RequestParam(value = "clientName", required = false) String clientName) throws NoSuchAlgorithmException {
        customClientDetailsService.verifyClient(clientName);
        return "redirect:/admin";
    }
}
