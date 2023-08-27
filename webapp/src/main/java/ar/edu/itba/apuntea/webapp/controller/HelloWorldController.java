package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.User;
import ar.edu.itba.apuntea.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HelloWorldController {
    private final UserService us;

    @Autowired
    public HelloWorldController(final UserService us) {
        this.us = us;
    }


    @RequestMapping("/")
    public ModelAndView helloWorld(){
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.createUser("email@", "mySecret"));
        return mav;
    }

    @RequestMapping("{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId){
        final ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("userid", userId);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm(){
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(
            @RequestParam(value = "email", required = true) final String email,
            @RequestParam(value = "password", required = true) final String password
    ){
        final User user = us.createUser(email, password);
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", user);
        return mav;
    }
}
