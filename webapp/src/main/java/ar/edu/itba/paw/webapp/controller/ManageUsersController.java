package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.BanUserForm;
import ar.edu.itba.paw.webapp.forms.DeleteWithReasonForm;
import ar.edu.itba.paw.webapp.forms.SearchUserForm;
import ar.edu.itba.paw.webapp.forms.UnbanUserForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.ws.http.HTTPException;
import java.util.UUID;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

@Controller
@RequestMapping("/manage/users")
public class ManageUsersController {
    private final SecurityService securityService;
    private final UserService userService;
    private final NoteService noteService;

    @Autowired
    public ManageUsersController(final SecurityService securityService, final UserService userService, final NoteService noteService) {
        this.securityService = securityService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView manageUsers (@ModelAttribute("unbanUserForm") final UnbanUserForm unbanUserForm,
                                     @Valid @ModelAttribute("searchForm") final SearchUserForm searchUserForm,
                                     final BindingResult result, final ModelMap model) {

        if (result.hasErrors())
            throw new HTTPException(400);

        ModelAndView mav = new ModelAndView("manage-users");

        addFormOrGetWithErrors(mav, model, BAN_USER_FORM_BINDING, "errorsBanUserForm", "banUserForm", BanUserForm.class);

        mav.addObject("banUserId", model.get(BAN_USER_ID));

        Page<User> users = userService.getStudents(searchUserForm.getQuery(), searchUserForm.getPageNumber());
        mav.addObject("maxPage", users.getTotalPages());
        mav.addObject("currentPage", users.getCurrentPage());
        mav.addObject("users", users.getContent());

        mav.addObject(USER_BANNED, model.getOrDefault(USER_BANNED, false));
        mav.addObject(USER_UNBANNED, model.getOrDefault(USER_UNBANNED, false));

        return mav;
    }

    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    public ModelAndView banUser(@Valid @ModelAttribute("banUserForm") final BanUserForm banUserForm,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes) {

        final ModelAndView mav = new ModelAndView("redirect:/manage/users");

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BAN_USER_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(BAN_USER_ID, banUserForm.getUserId());
        }
        else{
            userService.banUser(banUserForm.getUserId(), banUserForm.getReason());
            redirectAttributes.addFlashAttribute(USER_BANNED, true);
        }
        return mav;
    }

    @RequestMapping(value = "/unban", method = RequestMethod.POST)
    public ModelAndView unbanUser(@ModelAttribute("unbanUserForm") final UnbanUserForm unbanUserForm,
                                  final RedirectAttributes redirectAttributes) {

        userService.unbanUser(unbanUserForm.getUserId());
        redirectAttributes.addFlashAttribute(USER_UNBANNED, true);
        return new ModelAndView("redirect:/manage/users");
    }

    @RequestMapping(value = "/{userId}/review/{noteId}/delete", method = {RequestMethod.POST})
    public ModelAndView deleteReview(@PathVariable("noteId") @ValidUuid UUID noteId,
                                     @PathVariable("userId") @ValidUuid UUID userId,
                                     @Valid @ModelAttribute("deleteWithReasonForm") final DeleteWithReasonForm deleteWithReasonForm,
                                     final BindingResult result, final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(DELETE_WITH_REASON_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(DELETE_WITH_REASON_REVIEW, true);
            redirectAttributes.addFlashAttribute(REVIEW_USER_ID, userId.toString());
        }
        else {
            noteService.deleteReview(noteId, userId, deleteWithReasonForm.getReason());
            redirectAttributes.addFlashAttribute(REVIEW_DELETED, true);
        }

        return new ModelAndView("redirect:" + deleteWithReasonForm.getRedirectUrl());
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

