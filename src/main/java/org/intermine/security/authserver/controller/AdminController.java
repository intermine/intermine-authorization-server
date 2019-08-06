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

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClientDetailRepository iOauthClientDetails;

    @Autowired
    UserDetailRepository userDetailRepository;

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
