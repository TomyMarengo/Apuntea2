package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.BanUnbanUserForm;
import ar.edu.itba.paw.webapp.forms.SearchUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public ModelAndView manageUsers (@ModelAttribute("searchForm") final SearchUserForm searchUserForm,
                                     @ModelAttribute("banUserForm") final BanUnbanUserForm banUserForm,
                                     @ModelAttribute("unbanUserForm") final BanUnbanUserForm unbanUserForm
                                     ) {
        final ModelAndView mav = new ModelAndView("manage-users");

        List<User> users = userService.getStudents(searchUserForm.getQuery(), searchUserForm.getPageNumber());

        mav.addObject("maxPage", userService.getStudentsQuantity(searchUserForm.getQuery()));
        mav.addObject("users", users);

        return mav;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

