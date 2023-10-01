package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.InstitutionData;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ModelAndView index(@ModelAttribute("searchForm") final SearchForm searchForm) {
        // TODO: Remove index?
        return new ModelAndView("redirect:search");

//        ModelAndView mav = new ModelAndView("index");
//
//        securityService.getCurrentUserEmail().ifPresent(email -> mav.addObject("username", email));
//
//        mav.addObject("institutions", dataService.getInstitutions());
//        mav.addObject("careers", dataService.getCareers());
//        mav.addObject("subjects", dataService.getSubjects());
//
//        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/register")
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm userForm) {
        ModelAndView mav = new ModelAndView("register");

        // TODO: Ask if this should go in the service
        InstitutionData institutionData = dataService.getInstitutionData();
        mav.addObject("institutionData", new Gson().toJson(institutionData));
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerForm(userForm);
        }
        userService.create(userForm.getEmail(), userForm.getPassword(), userForm.getInstitutionId(), userForm.getCareerId(), Role.ROLE_STUDENT);
        return new ModelAndView("redirect:/login");
    }
}
