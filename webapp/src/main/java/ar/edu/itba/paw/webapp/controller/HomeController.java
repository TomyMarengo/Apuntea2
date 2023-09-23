package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final DataService dataService;
    private final UserService userService;
    private final SecurityService securityService;


    @Autowired
    public HomeController(final DataService dataService, final UserService userService, final SecurityService securityService) {
        this.dataService = dataService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/")
    public ModelAndView index(@ModelAttribute("searchNotesForm") final SearchNotesForm searchNotesForm) {
        ModelAndView mav = new ModelAndView("index");

        securityService.getCurrentUserEmail().ifPresent(email -> mav.addObject("username", email));

        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());

        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/register")
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm userForm) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        userService.create(userForm.getEmail(), userForm.getPassword()); //TODO: handle errors

        return new ModelAndView("redirect:/login");
    }
}
