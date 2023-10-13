package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.BanUnbanUserForm;
import ar.edu.itba.paw.webapp.forms.SearchUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

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
                                     @ModelAttribute("banUserForm") final BanUnbanUserForm banUserForm,
                                     @ModelAttribute("unbanUserForm") final BanUnbanUserForm unbanUserForm,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/errors/400");
        }

        final ModelAndView mav = new ModelAndView("manage-users");

        List<User> users = userService.getStudents(searchUserForm.getQuery(), searchUserForm.getPageNumber());
        mav.addObject("maxPage", userService.getStudentPages(searchUserForm.getQuery()));
        mav.addObject("users", users);

        return mav;
    }

    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    public ModelAndView banUser(@ModelAttribute("banUserForm") final BanUnbanUserForm banUserForm) {
        userService.banUser(banUserForm.getUserId());
        return new ModelAndView("redirect:/manage/users");
    }

    @RequestMapping(value = "/unban", method = RequestMethod.POST)
    public ModelAndView unbanUser(@ModelAttribute("unbanUserForm") final BanUnbanUserForm unbanUserForm) {
        userService.unbanUser(unbanUserForm.getUserId());
        return new ModelAndView("redirect:/manage/users");
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

