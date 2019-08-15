package org.intermine.security.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.UserClientTracker;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.ClientDetailRepository;
import org.intermine.security.authserver.repository.UserClientTrackerRepository;
import org.intermine.security.authserver.repository.UserDetailRepository;
import org.intermine.security.authserver.security.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;

@Controller
@SessionAttributes(types = AuthorizationRequest.class)
public class OauthController {
    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    @Autowired
    private UserClientTrackerRepository userClientTrackerRepository;

    private ClientDetailsService clientDetailsService;

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(@ModelAttribute AuthorizationRequest clientAuth, Principal principal) {
        String username = principal.getName();
        String clientId = clientAuth.getClientId();
        OauthClientDetails oauthClientDetails = iOauthClientDetails.findByClientId(clientId);
        TreeMap<String, Object> model = new TreeMap<String, Object>();
        String clientName = oauthClientDetails.getClientName();
        model.put("auth_request", clientAuth);
        model.put("clientName", clientName);
        boolean mergeRequest = false;
        if(clientAuth.getRequestParameters().get("mergeProfile")!=null){
            mergeRequest=true;
        }
        model.put("mergeRequest", mergeRequest);

        return new ModelAndView("confirmForm", model);
    }


    @PostMapping(value = {"/merge-request"}, params = "merge")
    public String merge(@ModelAttribute AuthorizationRequest clientAuth, Principal principal) {
        String mergeUrl=null;
        if(clientAuth.getRequestParameters().get("mergeUrl")!=null){
            mergeUrl=clientAuth.getRequestParameters().get("mergeUrl");
        }
        return "redirect:" + mergeUrl + "?merge=" + true;
    }

    @GetMapping("/account-status")
    @ResponseBody
    public ResponseEntity<?> updateAccountStatus(@RequestParam String sub, String clientId) {
        String userId = null;
        try {
            userId = Encryption.DecryptAESCBCPCKS5Padding(sub);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        assert userId != null;
        Users user = userDetailRepository.findByUserId(Integer.valueOf(userId));
        OauthClientDetails oauthClientDetails = iOauthClientDetails.findByClientId(clientId);
        userClientTrackerRepository.updateMerged(oauthClientDetails.getClientName(), user.getUsername(), true);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> currentUserName(HttpServletRequest request) throws IOException {
        String token=request.getHeader("Authorization").split("Bearer ")[1];
        ObjectMapper objectMapper = new ObjectMapper();
        Jwt jwt = JwtHelper.decode(token);
        Map claims = objectMapper.readValue(jwt.getClaims(), Map.class);
        String name = (String) claims.get("name");
        String email =(String) claims.get("email");
        String sub = (String) claims.get("sub");
        String mergeProfileId = (String) claims.get("mergeProfileId");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("sub",sub);
        if(mergeProfileId!=null){
            jsonObject.addProperty("mid",mergeProfileId);
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.FOUND);
    }

    @Autowired
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }
}
