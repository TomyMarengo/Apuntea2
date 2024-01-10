package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.InvalidException;
import ar.edu.itba.paw.models.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private	static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView notFound() {
        return new ModelAndView("forward:/404");
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView maxUploadSizeExceeded() {
        LOGGER.debug("Max upload size exceeded");
        ModelAndView mav = new ModelAndView("forward:/400");
        mav.addObject("maxUploadSizeExceeded", true);
        return mav;
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView invalidEntity(HttpServletRequest req, Exception ex) {
        LOGGER.debug("Request: {}, raised {}", req.getRequestURL(), ex.toString());
        return new ModelAndView("forward:/400");
    }

    @ExceptionHandler(HTTPException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView numberedError(HTTPException exception) {
        return new ModelAndView("forward:/" + exception.getStatusCode());
    }
}
