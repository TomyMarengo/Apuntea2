package ar.edu.itba.paw.webapp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    static final String BAN_USER_FORM_BINDING = "org.springframework.validation.BindingResult.banUserForm";
    static final String BAN_USER_ID = "banUserId";
    static final String REVIEW_UPLOADED = "reviewUploaded";
    static final String USER_EDITED = "userEdited";
    static final String NOTE_EDITED = "noteEdited";
    static final String DIRECTORY_CREATED = "directoryCreated";
    static final String DIRECTORY_EDITED = "directoryEdited";
    static final String NOTE_DELETED = "noteDeleted";
    static final String ITEMS_DELETED = "itemsDeleted";
    static final String DIRECTORY_DELETED = "directoryDeleted";
    static final String FAVORITE_ADDED = "favoriteAdded";
    static final String FAVORITE_REMOVED = "favoriteRemoved";
    static final String USER_BANNED = "userBanned";
    static final String USER_UNBANNED = "userUnbanned";
    static final String REVIEW_DELETED = "reviewDeleted";
    static final String LINK_SUBJECT_FORM_BINDING = "org.springframework.validation.BindingResult.linkSubjectForm";
    static final String UNLINK_SUBJECT_FORM_BINDING = "org.springframework.validation.BindingResult.unlinkSubjectForm";
    static final String CREATE_SUBJECT_FORM_BINDING = "org.springframework.validation.BindingResult.createSubjectForm";
    static final String EDIT_SUBJECT_FORM_BINDING = "org.springframework.validation.BindingResult.editSubjectForm";
    static final String DELETE_WITH_REASON_FORM_BINDING = "org.springframework.validation.BindingResult.deleteWithReasonForm";
    static final String DELETE_WITH_REASON_REVIEW = "deleteWithReasonReview";
    static final String DELETE_WITH_REASON_NOTE = "deleteWithReasonNote";
    static final String REVIEW_USER_ID = "reviewUserId";
    static final String DELETE_NOTE_IDS = "deleteNotesIds";
    static final String DELETE_DIRECTORY_IDS = "deleteDirectoriesIds";
    static final String SUBJECT_LINKED = "subjectLinked";
    static final String SUBJECT_UNLINKED = "subjectUnlinked";
    static final String SUBJECT_CREATED = "subjectCreated";
    static final String SUBJECT_EDITED = "subjectEdited";
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

    static String toSafeJson(Object object) {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(object);
    }
}


