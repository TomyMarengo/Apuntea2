package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.BanUserForm;
import ar.edu.itba.paw.webapp.forms.SearchUserForm;
import ar.edu.itba.paw.webapp.forms.UnbanUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.BAN_USER;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.UNBAN_USER;

@Controller
@RequestMapping("/manage/users")
public class ManageUsersController {
    private final SecurityService securityService;
    private final UserService userService;

    @Autowired
    public ManageUsersController(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
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

        List<User> users = userService.getStudents(searchUserForm.getQuery(), searchUserForm.getPageNumber());
        mav.addObject("maxPage", userService.getStudentPages(searchUserForm.getQuery()));
        mav.addObject("users", users);
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

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

