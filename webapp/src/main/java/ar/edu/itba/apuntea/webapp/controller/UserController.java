package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.services.NoteService;
import ar.edu.itba.apuntea.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ar.edu.itba.apuntea.services.UserService;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId){
        final ModelAndView mav = new ModelAndView("create");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

}