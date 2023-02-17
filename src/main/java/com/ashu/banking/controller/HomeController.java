package com.ashu.banking.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ashu.banking.dao.RoleDao;
import com.ashu.banking.domain.PrimaryAccount;
import com.ashu.banking.domain.SavingsAccount;
import com.ashu.banking.domain.User;
import com.ashu.banking.domain.security.UserRole;
import com.ashu.banking.service.UserService;
import com.ashu.banking.util.AppConstants;

@Controller
public class HomeController {
	private static final Logger L = LogManager.getLogger(HomeController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
    private RoleDao roleDao;
	
	@RequestMapping(AppConstants.HOME_REDIRECT_INDEX)
	public String home() {
		L.debug("35 : HomeController.home(...)");
		return "redirect:/index";
	}

	@RequestMapping(AppConstants.HOME_INDEX)
    public String index() {
		L.debug("41 : HomeController.index(...)");
        return "index";
    }
	
	@RequestMapping(value = AppConstants.HOME_SIGNUP, method = RequestMethod.GET)
    public String signup(Model model) {
		L.debug("47 : Start : HomeController.signup(...)");
        User user = new User();

        model.addAttribute("user", user);
        L.debug("51 : End : HomeController.signup(...)");
        return "signup";
    }
	
	@RequestMapping(value = AppConstants.HOME_SIGNUP, method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user,  Model model) {
		L.debug("57 : Start : HomeController.signupPost(...)");
        if(userService.checkUserExists(user.getUsername(), user.getEmail()))  {

            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }

            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }
            L.debug("67 : End : HomeController.signupPost(...)");
            return "signup";
        } else {
        	 Set<UserRole> userRoles = new HashSet<>();
             userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));

            userService.createUser(user, userRoles);
            L.debug("74 : End : HomeController.signupPost(...)");
            return "redirect:/";
        }
    }

	@RequestMapping(AppConstants.HOME_USER_FRONT)
	public String bankHome(Principal principal, Model model) {
		L.debug("81 : Start : HomeController.bankHome(...)");
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingsAccount", savingsAccount);
        L.debug("88 : End : HomeController.bankHome(...)");
        return "bankHome";
    }
}
