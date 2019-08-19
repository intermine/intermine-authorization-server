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

/**
 * Oauth controller contains endpoints which are
 * accessed during Oauth flow.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@SessionAttributes(types = AuthorizationRequest.class)
public class OauthController {

    /**
     * An object of jpa repository to query users table in database.
     */
    @Autowired
    UserDetailRepository userDetailRepository;

    /**
     * An object of jpa repository to query oauth_client_details table in database.
     */
    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    /**
     * An object of jpa repository to query userclienttracker table in database.
     */
    @Autowired
    private UserClientTrackerRepository userClientTrackerRepository;

    /**
     * Object of spring default clietdetailservice.
     */
    private ClientDetailsService clientDetailsService;

    /**
     * <p>Mapping for /oauth/confirm_access path.This end point
     * is used by default spring oauth2 security, /oauth/authorize
     * end point calls this path to get permission from user to
     * authorize client to access information like name and email.
     *This method returns user confirmForm template which has two
     * pop ups to get permission from user to merge previous
     * account of mine if have any and to authorize client to access
     * name and email.
     * </p>
     *
     * @param clientAuth An authorization request form client
     * @param principal Current logged in user
     * @return confirmForm template with some attributes in model
     */
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


    /**
     * <p>Mapping for /merge-request path. This end point is called
     * when users taps on Yes to merge his/her previous account of mine
     * and redirect user to login page of mine with merge parameter
     * as true.
     * </p>
     *
     * @param clientAuth An authorization request form client
     * @param principal Current logged in user
     * @return redirect to login page of mine
     */
    @PostMapping(value = {"/merge-request"}, params = "merge")
    public String merge(@ModelAttribute AuthorizationRequest clientAuth, Principal principal) {
        String mergeUrl=null;
        if(clientAuth.getRequestParameters().get("mergeUrl")!=null){
            mergeUrl=clientAuth.getRequestParameters().get("mergeUrl");
        }
        return "redirect:" + mergeUrl + "?merge=" + true;
    }

    /**
     * <p>Mapping for /account-status path. This method can be used
     * to update user client tracker table if user is successfully merged
     * his/her previous mine account with this new IM account.
     * </p>
     *
     * @param sub unique id of IM user who merged with mine account
     * @param clientId Id of client which is making request
     * @return ok httpstatus in responseEntity
     */
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

    /**
     * <p>Get Mapping for /user-info path. This endpoint is used to
     * return user info like name and email back to client after
     * successful oauth2 authorization. This method decodes the JWT
     * token from header and send back the details to client.
     * </p>
     *
     * @param request HttpServletRequest instance
     * @return Json object of user details.
     */
    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) throws IOException {
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

    /**
     * <p>This method set clientdetailservice during Oauth flow.
     * i.e used when accessing /oauth/confirm_access path.
     * </p>
     *
     * @param clientDetailsService Instance of default ClientDetailsService of spring security
     */
    @Autowired
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }
}
