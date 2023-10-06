package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.EditUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.services.UserService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
    public ModelAndView settings(@ModelAttribute final EditUserForm editUserForm) {
        ModelAndView mav = new ModelAndView("settings");
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return new ModelAndView("settings");
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView updateSettings(@ModelAttribute("user") User user,
            @Valid @ModelAttribute final EditUserForm editUserForm,
            final BindingResult result) {
        ModelAndView mav = new ModelAndView("settings");
        if(!result.hasErrors()){
            userService.update(new User(editUserForm.getFirstName(), editUserForm.getLastName(), editUserForm.getUsername()));
        }
        else
            mav.addObject("errorsEditUserForm", result.getAllErrors());
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return mav;
    }
}