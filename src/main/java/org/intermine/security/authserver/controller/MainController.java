package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    CustomClientDetailsService customClientDetailsService;


    @RequestMapping(value = "/client-registration", method = RequestMethod.POST)
    public Map<String, String> save(@RequestBody OauthClientDetails oauthClientDetails)throws NoSuchAlgorithmException {
        return customClientDetailsService.addCustomClientDetails(oauthClientDetails);
    }
    
    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> currentUserName(Authentication authentication) {
        Users user= (Users) authentication.getPrincipal();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("username",user.getUsername());
        map.put("email",user.getEmail());
        return map;
    }

}
