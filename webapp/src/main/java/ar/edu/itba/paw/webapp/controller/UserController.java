package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile() {
        return new ModelAndView("profile");
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView settings() {
        return new ModelAndView("settings");
    }
}