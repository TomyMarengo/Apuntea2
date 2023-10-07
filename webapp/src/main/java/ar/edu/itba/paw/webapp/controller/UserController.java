package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.ChangePasswordForm;
import ar.edu.itba.paw.webapp.forms.EditUserForm;
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

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;
    private final DirectoryService directoryService;
    private final SecurityService securityService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ServletContext context;

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
    public ModelAndView settings(@ModelAttribute final EditUserForm editUserForm,
                                 @ModelAttribute final ChangePasswordForm changePasswordForm) {
        ModelAndView mav = new ModelAndView("settings");
        mav.addObject("user", this.securityService.getCurrentUserOrThrow());
        return new ModelAndView("settings");
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView updateSettings(@ModelAttribute("user") User user,
            @ModelAttribute final ChangePasswordForm changePasswordForm,
            @Valid @ModelAttribute final EditUserForm editUserForm,
            final BindingResult result) {
        ModelAndView mav = new ModelAndView("settings");
        if(!result.hasErrors()) {
            try {
                userService.update(new User(editUserForm.getFirstName(), editUserForm.getLastName(), editUserForm.getUsername()), editUserForm.getProfilePicture());
            } catch (InvalidFileException e) {
                mav.addObject("invalidFileError"); // TODO: Add modals for this error
            }
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
        ModelAndView mav = new ModelAndView("settings");
        if(!result.hasErrors()) {
            if (!changePasswordForm.getOldPassword().equals(changePasswordForm.getNewPassword()))
                userService.updateCurrentUserPassword(changePasswordForm.getNewPassword());
        }
        else
            mav.addObject("errorsChangePasswordForm", result.getAllErrors());
        return mav;
    }


    @RequestMapping(value = "/{userId}/profile/picture", method = RequestMethod.GET, produces = {"image/jpeg", "image/png"})
    @ResponseBody
    public byte[] getProfilePicture(@PathVariable("userId") @ValidUuid UUID userId)  {
        return userService.getProfilePicture(userId).orElseGet(() -> {
            try {
                return IOUtils.toByteArray(context.getResourceAsStream("image/profile-picture.jpeg"));
            } catch (IOException e) {
                LOGGER.error("Error while reading default profile picture", e);
                return null;
            }
        });
    }
}