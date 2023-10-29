package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.institutional.dtos.InstitutionDataDto;
import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.forms.search.SearchForm;
import ar.edu.itba.paw.webapp.forms.user.password.ChallengeForm;
import ar.edu.itba.paw.webapp.forms.user.password.ForgotPasswordForm;
import ar.edu.itba.paw.webapp.forms.user.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.toSafeJson;

@Controller
public class HomeController {

    private final InstitutionService institutionService;
    private final UserService userService;
    private final SecurityService securityService;
    private final VerificationCodesService verificationCodesService;


    @Autowired
    public HomeController(final InstitutionService institutionService, final UserService userService, final SecurityService securityService, final VerificationCodesService verificationCodesService) {
        this.institutionService = institutionService;
        this.userService = userService;
        this.securityService = securityService;
        this.verificationCodesService = verificationCodesService;
    }

    @RequestMapping(value = "/")
    public ModelAndView index(@ModelAttribute("searchForm") final SearchForm searchForm) {

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", this.securityService.getCurrentUser().orElse(null));
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "success", required = false) final boolean success, @RequestParam(value = "email", required = false) final String email) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("success", success);
        mav.addObject("email", email);
        return mav;
    }

    @RequestMapping(value = "/register")
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm userForm) {
        ModelAndView mav = new ModelAndView("register");
        InstitutionDataDto institutionDataDto = institutionService.getInstitutionData();
        mav.addObject("institutionData", toSafeJson(institutionDataDto));
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerForm(userForm);
        }
        userService.create(userForm.getEmail(), userForm.getPassword(), userForm.getCareerId(), Role.ROLE_STUDENT);
        return new ModelAndView("redirect:/login?success=true");
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public ModelAndView forgotPassword(@ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm) {
        return new ModelAndView("forgot-password");
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView forgotPassword(@ModelAttribute("challengeForm") final ChallengeForm challengeForm,
            @Valid @ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return forgotPassword(forgotPasswordForm);
        }
        verificationCodesService.sendForgotPasswordCode(forgotPasswordForm.getEmail());
        ModelAndView mav = new ModelAndView("challenge");
        mav.addObject("email", forgotPasswordForm.getEmail());
        return mav;
    }

    @RequestMapping(value = "/challenge", method = RequestMethod.POST)
    public ModelAndView challenge(@Valid @ModelAttribute("challengeForm") final ChallengeForm challengeForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("challenge").addObject("email", challengeForm.getEmail());
        }
        if (userService.updateUserPasswordWithCode(challengeForm.getEmail(), challengeForm.getCode(), challengeForm.getNewPassword())) {
            return new ModelAndView("redirect:/login?email=" + challengeForm.getEmail());
        }
        return new ModelAndView("challenge").addObject("invalidCode", true)
                .addObject("email", challengeForm.getEmail());
    }


}
