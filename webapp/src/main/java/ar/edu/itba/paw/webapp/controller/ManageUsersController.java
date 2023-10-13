package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.BanUserForm;
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
    public ModelAndView manageUsers (@Valid @ModelAttribute("searchForm") final SearchUserForm searchUserForm,
                                     @ModelAttribute("banUserForm") final BanUserForm banUserForm,
                                     @ModelAttribute("unbanUserForm") final UnbanUserForm unbanUserForm,
                                     BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("/errors/400");
        }

        final ModelAndView mav = new ModelAndView("manage-users");

        Page<User> users = userService.getStudents(searchUserForm.getQuery(), searchUserForm.getPageNumber());
        mav.addObject("maxPage", users.getTotalPages());
        mav.addObject("users", users.getContent());
        
        mav.addObject(BAN_USER, model.getOrDefault(BAN_USER, false));
        mav.addObject(UNBAN_USER, model.getOrDefault(UNBAN_USER, false));

        return mav;
    }

    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    public ModelAndView banUser(@ModelAttribute("banUserForm") final BanUserForm banUserForm, RedirectAttributes redirectAttributes) {
        userService.banUser(banUserForm.getUserId(), banUserForm.getReason());
        redirectAttributes.addFlashAttribute(BAN_USER, true);
        return new ModelAndView("redirect:/manage/users");
    }

    @RequestMapping(value = "/unban", method = RequestMethod.POST)
    public ModelAndView unbanUser(@ModelAttribute("unbanUserForm") final UnbanUserForm unbanUserForm, RedirectAttributes redirectAttributes) {
        userService.unbanUser(unbanUserForm.getUserId());
        redirectAttributes.addFlashAttribute(UNBAN_USER, true);
        return new ModelAndView("redirect:/manage/users");
    }

    @RequestMapping(value = "/{userId}/review/{noteId}/delete", method = {RequestMethod.POST})
    public ModelAndView deleteReview(@PathVariable("noteId") @ValidUuid UUID noteId,
                                     @PathVariable("userId") @ValidUuid UUID userId,
                                     @RequestParam(required = false) @Size(max = 300) String reason,
                                     final RedirectAttributes redirectAttributes) {
        noteService.deleteReview(noteId, userId, reason);
        redirectAttributes.addFlashAttribute(DELETE_REVIEW, true);
        return new ModelAndView("redirect:/notes/"+noteId);
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

