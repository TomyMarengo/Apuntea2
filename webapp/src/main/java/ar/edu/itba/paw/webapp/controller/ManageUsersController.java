package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.SecurityService;
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

    @Autowired
    public ManageUsersController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView manageUsers (@ModelAttribute("searchForm") final SearchUserForm searchUserForm,
                                     @ModelAttribute("banUserForm") final BanUnbanUserForm banUserForm,
                                     @ModelAttribute("unbanUserForm") final BanUnbanUserForm unbanUserForm
                                     ) {
        final ModelAndView mav = new ModelAndView("manage-users");

        // Hardcode a list of users with username, email, roles (array string) and status (boolean)
        List<User> users = new ArrayList<>();
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user1", "user1@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user2", "user2@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user3", "user3@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user4", "user4@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user5", "user5@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user6", "user6@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user7", "user7@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user8", "user8@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user9", "user9@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user10", "user10@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user11", "user11@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user12", "user12@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user13", "user13@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user14", "user14@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user15", "user15@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user16", "user16@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user17", "user17@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user18", "user18@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user19", "user19@gmail.com", new String[]{"ROLE_STUDENT"}, false));
        users.add(new User(UUID.fromString("64cf2517-4683-4016-b443-6d38d85939b3"), "user20", "user20@gmail.com", new String[]{"ROLE_STUDENT", "ROLE_ADMIN"}, true));

        mav.addObject("maxPage", 5);
        mav.addObject("users", users);

        return mav;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}

