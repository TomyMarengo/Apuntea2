package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.forms.user.password.ChangePasswordForm;
import ar.edu.itba.paw.webapp.forms.user.EditUserForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.USER_EDITED;

@Controller
public class UserController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final DirectoryService directoryService;
    private final SecurityService securityService;

    private final CareerService careerService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ServletContext context;

    @Autowired
    public UserController(final SubjectService subjectService, final UserService userService, final SecurityService securityService, final DirectoryService directoryService, final CareerService careerService) {
        this.userService = userService;
        this.securityService = securityService;
        this.directoryService = directoryService;
        this.subjectService = subjectService;
        this.careerService = careerService;
    }

    @RequestMapping(value = "/my-favorites", method = RequestMethod.GET)
    public ModelAndView myFavorites() {
        ModelAndView mav = new ModelAndView("my-favorites");
        User user = securityService.getCurrentUserOrThrow();
        mav.addObject("user", user);
        mav.addObject("favorites", directoryService.getFavorites());
        return mav;
    }

    @RequestMapping(value = "/my-subjects", method = RequestMethod.GET)
    public ModelAndView mySubjects() {
        ModelAndView mav = new ModelAndView("my-subjects");
        User user = securityService.getCurrentUserOrThrow();
        mav.addObject("user", user);
        mav.addObject("root_directories", subjectService.getSubjectsByCareerGroupByYear());
        return mav;
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(@ModelAttribute final EditUserForm editUserForm,
                                 @ModelAttribute final ChangePasswordForm changePasswordForm) {
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("careers", ControllerUtils.toSafeJson(careerService.getCareersByCurrentUserInstitution()));
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return mav;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView updateProfile(@ModelAttribute final ChangePasswordForm changePasswordForm,
            @Valid @ModelAttribute final EditUserForm editUserForm,
            final BindingResult result) {
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("careers", ControllerUtils.toSafeJson(careerService.getCareersByCurrentUserInstitution()));

        if(!result.hasErrors()) {
            userService.updateProfile(editUserForm.getFirstName(), editUserForm.getLastName(), editUserForm.getUsername(), editUserForm.getProfilePicture(), editUserForm.getCareerId());
            mav.addObject(USER_EDITED, true);
        }
        else
            mav.addObject("errorsEditUserForm", result.getAllErrors());
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return mav;
    }

    @RequestMapping(value = "/profile/{userId}/picture", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getProfilePicture(@PathVariable("userId") @ValidUuid UUID userId)  {
        return userService.getProfilePicture(userId).orElseGet(() -> {
            try {
                return IOUtils.toByteArray(context.getResourceAsStream("image/profile-picture.jpeg"));
            } catch (IOException e) {
                LOGGER.warn("Error while reading default profile picture");
                return null;
            }
        });
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public ModelAndView getPassword(@RequestParam(value = "password-changed", required = false) final boolean passwordChanged,
                                    @ModelAttribute final ChangePasswordForm changePasswordForm) {
        User user = securityService.getCurrentUserOrThrow();
        ModelAndView mav = new ModelAndView("change-password");
        mav.addObject("user", user);
        mav.addObject("passwordChanged", passwordChanged);
        return mav;
    }
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute final ChangePasswordForm changePasswordForm,
                                       final BindingResult result) {
        if(!result.hasErrors()) {
            userService.updateCurrentUserPassword(changePasswordForm.getNewPassword());
            return new ModelAndView("redirect:change-password?password-changed=true");
        }
        return new ModelAndView("change-password").addObject("errorsChangePasswordForm", result.getAllErrors());
    }
}