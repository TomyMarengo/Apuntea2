package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.DirectoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class, NoteNotFoundException.class, DirectoryNotFoundException.class})

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("/errors/404");
    }
}
