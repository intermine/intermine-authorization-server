package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.model.OauthClientDetails;
import org.intermine.security.authserver.model.Users;
import org.intermine.security.authserver.repository.ClientDetailRepository;
import org.intermine.security.authserver.repository.UserDetailRepository;
import org.intermine.security.authserver.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

/**
 * Admin controller contains endpoint for adminPage
 * template.
 *
 * @author Rahul Yadav
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * An object of jpa repository to query oauth_client_details table in database.
     */
    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    /**
     * An object of jpa repository to query users table in database.
     */
    @Autowired
    UserDetailRepository userDetailRepository;

    /**
     * <p>Get Mapping for / path of admin controller. This method adds
     * required attributes in model for admin page template before
     * returning to it.
     *  </p>
     *
     * @param model An Instance of Model class
     * @param principal Current logged in user, can be obtained from SecurityContex too
     * @return AdminPage template
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        List<OauthClientDetails> clientList=iOauthClientDetails.findAll();
        List<Users> usersList=userDetailRepository.findAll();
        Long unverfiedClients=iOauthClientDetails.countByStatus(false);
        model.addAttribute("unverfiedClients",unverfiedClients);
        model.addAttribute("totalClients",clientList.size());
        model.addAttribute("totalUsers",usersList.size());
        model.addAttribute("clientList", clientList);
        model.addAttribute("usersList",usersList);
        return "adminPage";
    }
}
