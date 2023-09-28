package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class ControllerUtils {
    static final String CREATE_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.createNoteForm";
    static final String EDIT_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.editNoteForm";
    static final String EDIT_NOTE_ID = "editNoteId";
    static final String CREATE_DIRECTORY_FORM_BINDING = "org.springframework.validation.BindingResult.createDirectoryForm";
    static final String EDIT_DIRECTORY_ID = "editDirectoryId";
    static final String EDIT_DIRECTORY_FORM_BINDING = "org.springframework.validation.BindingResult.editDirectoryForm";
    static final String CREATE_REVIEW_FORM_BINDING = "org.springframework.validation.BindingResult.reviewForm";
    private	static final Logger LOGGER	= LoggerFactory.getLogger(ControllerUtils.class);

    private ControllerUtils(){}

    static void addFormOrGetWithErrors(ModelAndView mav, ModelMap model, String attribute, String errorName, String formName, Class<?> form) {
        if(model.containsAttribute(attribute)) {
            mav.addObject(errorName, ((BindingResult) model.get(attribute)).getAllErrors());
        } else {
            try {
                mav.addObject(formName, form.newInstance());
            } catch (InstantiationException ex) {
                LOGGER.error("Instantiation exception creating form {}", formName);
            } catch (IllegalAccessException ex) {
                LOGGER.error("Illegal access exception creating form {}", formName);
            }
        }
    }
}


