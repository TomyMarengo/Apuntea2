package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.forms.user.password.ChangePasswordForm;
import ar.edu.itba.paw.webapp.forms.user.EditUserForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.services.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ServletContext context;

    @Autowired
    public UserController(final SubjectService subjectService, final UserService userService, final SecurityService securityService, final DirectoryService directoryService) {
        this.userService = userService;
        this.securityService = securityService;
        this.directoryService = directoryService;
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("profile");
        User user = securityService.getCurrentUserOrThrow();
        mav.addObject("user", user);
        mav.addObject("root_directories", subjectService.getSubjectsByCareerGroupByYear());
        mav.addObject("favorites", directoryService.getFavorites());
        return mav;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView settings(@RequestParam(value = "password-changed", required = false) final boolean passwordChanged,
                                 @ModelAttribute final EditUserForm editUserForm,
                                 @ModelAttribute final ChangePasswordForm changePasswordForm) {
        ModelAndView mav = new ModelAndView("settings");

        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        mav.addObject("passwordChanged", passwordChanged);
        return mav;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView updateSettings(@ModelAttribute("user") User user,
            @ModelAttribute final ChangePasswordForm changePasswordForm,
            @Valid @ModelAttribute final EditUserForm editUserForm,
            final BindingResult result) {
        ModelAndView mav = new ModelAndView("settings");
        if(!result.hasErrors()) {
            userService.updateProfile(editUserForm.getFirstName(), editUserForm.getLastName(), editUserForm.getUsername(), editUserForm.getProfilePicture());
            mav.addObject(USER_EDITED, true);
        }
        else
            mav.addObject("errorsEditUserForm", result.getAllErrors());
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return mav;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@ModelAttribute final EditUserForm editUserForm,
                                        @Valid @ModelAttribute final ChangePasswordForm changePasswordForm,
                                       final BindingResult result) {
        if(!result.hasErrors()) {
            if (!changePasswordForm.getOldPassword().equals(changePasswordForm.getNewPassword()))
                userService.updateCurrentUserPassword(changePasswordForm.getNewPassword());
            return new ModelAndView("redirect:settings?password-changed=true");
        }
        return new ModelAndView("settings").addObject("errorsChangePasswordForm", result.getAllErrors());
    }


    @RequestMapping(value = "/profile/{userId}/picture", method = RequestMethod.GET, produces = {"image/jpeg", "image/png"})
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
}