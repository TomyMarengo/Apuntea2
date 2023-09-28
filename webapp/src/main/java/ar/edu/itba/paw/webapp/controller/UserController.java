package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;
    private final DirectoryService directoryService;
    private final SecurityService securityService;

    @Autowired
    public UserController(final UserService userService, final SecurityService securityService, final DirectoryService directoryService) {
        this.userService = userService;
        this.securityService = securityService;
        this.directoryService = directoryService;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("profile");
        User user = securityService.getCurrentUserOrThrow();
        mav.addObject("user", user);
        // TODO: Should this be moved to the service???
        mav.addObject("root_directories", directoryService.getRootDirectoriesByCurrentUserCareer());
        return mav;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView settings() {
        return new ModelAndView("settings");
    }


}