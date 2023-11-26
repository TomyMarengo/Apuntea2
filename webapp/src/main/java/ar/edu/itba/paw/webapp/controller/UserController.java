package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.forms.admin.DeleteWithReasonForm;
import ar.edu.itba.paw.webapp.forms.note.SearchReviewForm;
import ar.edu.itba.paw.webapp.forms.user.password.ChangePasswordForm;
import ar.edu.itba.paw.webapp.forms.user.EditUserForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.UUID;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

@Controller
public class UserController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final DirectoryService directoryService;
    private final NoteService noteService;
    private final SecurityService securityService;
    private final CareerService careerService;
    private final SearchService searchService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ServletContext context;

    @Autowired
    public UserController(final SubjectService subjectService,
                          final UserService userService,
                          final SecurityService securityService,
                          final DirectoryService directoryService,
                          final CareerService careerService,
                          final NoteService noteService,
                          final SearchService searchService
        ) {
        this.userService = userService;
        this.securityService = securityService;
        this.directoryService = directoryService;
        this.subjectService = subjectService;
        this.careerService = careerService;
        this.noteService = noteService;
        this.searchService = searchService;
    }

    @RequestMapping(value = "/my-favorites", method = RequestMethod.GET)
    public ModelAndView myFavorites() {
        ModelAndView mav = new ModelAndView("my-favorites");
        User user = securityService.getCurrentUserOrThrow();
        DirectoryFavoriteGroups favoriteGroups = directoryService.getFavorites();
        mav.addObject("user", user);
        mav.addObject("directoryFavorites", favoriteGroups.getDirectoryList());
        mav.addObject("subjectFavorites", favoriteGroups.getRootDirectoryList());
        mav.addObject("noteFavorites", noteService.getFavorites());
        mav.addObject("userFavorites", userService.getFollows());
        return mav;
    }

    @RequestMapping(value = "/user/{userId}/follow", method = RequestMethod.POST)
    public ModelAndView followUser(@PathVariable("userId") @ValidUuid UUID userId) {
        userService.follow(userId);
        return new ModelAndView("redirect:/user/" + userId + "/note-board");
    }

    @RequestMapping(value = "/user/{userId}/unfollow", method = RequestMethod.POST)
    public ModelAndView unfollowUser(@PathVariable("userId") @ValidUuid UUID userId) {
        userService.unfollow(userId);
        return new ModelAndView("redirect:/user/" + userId + "/note-board");
    }

    @RequestMapping(value = "/my-career", method = RequestMethod.GET)
    public ModelAndView myCareer() {
        ModelAndView mav = new ModelAndView("my-career");
        User user = securityService.getCurrentUserOrThrow();
        mav.addObject("user", user);
        mav.addObject("root_directories", subjectService.getSubjectsByCareerGroupByYear());
        return mav;
    }

    @RequestMapping(value = "/user/{userId}/note-board", method = RequestMethod.GET)
    public ModelAndView noteBoard(@PathVariable("userId") @ValidUuid UUID userId) {
        ModelAndView mav = new ModelAndView("note-board");
        User user = securityService.getCurrentUser().orElse(null);
        User owner =  userService.getOwner(userId, user);
        mav.addObject("user", user);
        mav.addObject("owner",owner);
        mav.addObject("ownerScore", userService.getAvgScore(owner.getUserId()));

        if (user != null && !user.getUserId().equals(userId))
            mav.addObject("isFollowing", userService.isFollowing(userId));

        mav.addObject("latestNotes", searchService.search(null,null, null, userId, "note",
                null, "date", false, 1, 10).getContent());
        mav.addObject("root_directories", subjectService.getSubjectsByUserIdGroupByYear(userId));
        return mav;
    }

    @RequestMapping(value = "/user/note-board", method = RequestMethod.GET)
    public ModelAndView myNoteBoard(){
        User user = securityService.getCurrentUserOrThrow();
        return new ModelAndView("redirect:/user/" + user.getUserId() + "/note-board");
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

    @RequestMapping(value = "/user/{userId}/reviews", method = RequestMethod.GET)
    public ModelAndView getReviews(@PathVariable("userId") @ValidUuid UUID userId,
                                   @Valid @ModelAttribute("searchForm") final SearchReviewForm searchReviewForm,
                                   final BindingResult result) {
        if (result.hasErrors())
            throw new HTTPException(400);

        final ModelAndView mav = new ModelAndView("reviews");
        User user = securityService.getCurrentUser().orElse(null);
        User owner =  userService.getOwner(userId, user);
        mav.addObject("user", user);
        mav.addObject("owner",owner);
        mav.addObject("ownerScore", userService.getAvgScore(owner.getUserId()));
        Page<Review> reviews = noteService.getPaginatedReviewsByUser(userId, searchReviewForm.getPageNumber(), searchReviewForm.getPageSize()); //TODO: traer tambien los datos del apunte
        mav.addObject("maxPage", reviews.getTotalPages());
        mav.addObject("currentPage", reviews.getCurrentPage());
        mav.addObject("reviews", reviews.getContent());
        return mav;
    }


    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    public ModelAndView configuration(ModelMap model) {
        User user = securityService.getCurrentUserOrThrow();
        ModelAndView mav = new ModelAndView("configuration");
        mav.addObject("user", user);
        mav.addObject("notificationsEnabled", user.hasNotificationsEnabled());
        addFormOrGetWithErrors(mav, model, CHANGE_PASSWORD_FORM_BINDING, "errorsChangePassword", "changePasswordForm", ChangePasswordForm.class);
        mav.addObject(PASSWORD_CHANGED, model.getOrDefault(PASSWORD_CHANGED, false));
        return mav;
    }
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute final ChangePasswordForm changePasswordForm,
                                       final BindingResult result, final RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CHANGE_PASSWORD_FORM_BINDING, result);
        }
        else{
            userService.updateCurrentUserPassword(changePasswordForm.getNewPassword());
            redirectAttributes.addFlashAttribute(PASSWORD_CHANGED, true);
        }
        return new ModelAndView("redirect:/configuration");
    }

    @RequestMapping(value = "/enable-notifications", method = RequestMethod.POST)
    public ModelAndView receiveNotifications() {
        userService.updateNotificationsEnabled(true);
        return new ModelAndView("redirect:/configuration");
    }

    @RequestMapping(value = "/disable-notifications", method = RequestMethod.POST)
    public ModelAndView dontReceiveNotifications() {
        userService.updateNotificationsEnabled(false);
        return new ModelAndView("redirect:/configuration");
    }


}