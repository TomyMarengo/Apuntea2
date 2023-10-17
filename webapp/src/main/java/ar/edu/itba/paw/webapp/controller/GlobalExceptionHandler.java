package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.institutional.CareerNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.DirectoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectCareerException;
import ar.edu.itba.paw.models.exceptions.note.InvalidNoteException;
import ar.edu.itba.paw.models.exceptions.note.InvalidReviewException;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.ws.http.HTTPException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class, NoteNotFoundException.class, DirectoryNotFoundException.class, CareerNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView notFound() {
        return new ModelAndView("forward:/404");
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView maxUploadSizeExceeded() {
        ModelAndView mav = new ModelAndView("forward:/400");
        mav.addObject("maxUploadSizeExceeded", true);
        return mav;
    }

    @ExceptionHandler({InvalidUserException.class,
            InvalidReviewException.class,
            InvalidNoteException.class,
            InvalidDirectoryException.class,
            InvalidFileException.class,
            InvalidSubjectCareerException.class,
            InvalidSubjectException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView invalidEntity() {
        return new ModelAndView("forward:/400");
    }

    @ExceptionHandler(HTTPException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView numberedError(HTTPException exception) {
        return new ModelAndView("forward:/" + exception.getStatusCode());
    }
}
