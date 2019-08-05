package org.intermine.security.authserver.controller;

import org.intermine.security.authserver.utils.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "adminPage";
    }
}
