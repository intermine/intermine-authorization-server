package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    CustomClientDetailsService customClientDetailsService;


    @RequestMapping(value = "/client-registration", method = RequestMethod.POST)
    public Map<String, String> save(@RequestBody OauthClientDetails oauthClientDetails)throws NoSuchAlgorithmException {
        return customClientDetailsService.addCustomClientDetails(oauthClientDetails);
    }

}