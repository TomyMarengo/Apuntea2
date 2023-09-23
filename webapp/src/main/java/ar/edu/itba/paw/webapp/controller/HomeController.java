package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.CustomUserDetails;
import ar.edu.itba.paw.webapp.forms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
public class HomeController {

    private final DataService dataService;
    private final UserService userService;
    private final SecurityService securityService;
    private static final String CREATE_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.createNoteForm";

    @Autowired
    public HomeController(final DataService dataService, final UserService userService, final SecurityService securityService) {
        this.dataService = dataService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/")
    public ModelAndView index(@ModelAttribute("searchNotesForm") final SearchNotesForm searchNotesForm,
                              ModelMap model) {
        ModelAndView mav = new ModelAndView("index");
        securityService.getCurrentUserEmail().ifPresent(email -> mav.addObject("username", email));
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());

        if (model.containsAttribute(CREATE_NOTE_FORM_BINDING)) {
            mav.addObject("errors", ((BindingResult) model.get(CREATE_NOTE_FORM_BINDING)).getAllErrors());
        } else {
            mav.addObject("createNoteForm", new CreateNoteForm());
        }
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

        userService.create(userForm.getEmail(), userForm.getPassword());

        return new ModelAndView("redirect:/login" );
    }
}
