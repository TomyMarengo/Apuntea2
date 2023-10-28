package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

import ar.edu.itba.paw.webapp.forms.admin.DeleteWithReasonForm;
import ar.edu.itba.paw.webapp.forms.note.EditNoteForm;
import ar.edu.itba.paw.webapp.forms.note.ReviewForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;


@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final DirectoryService directoryService;
    private final SecurityService securityService;

    @Autowired
    public NoteController(NoteService noteService, DirectoryService directoryService, SecurityService securityService) {
        this.noteService = noteService;
        this.directoryService = directoryService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.GET)
    public ModelAndView getNote(@PathVariable("noteId") @ValidUuid UUID noteId, final ModelMap model) {

        final ModelAndView mav = new ModelAndView("note");

        addFormOrGetWithErrors(mav, model, DELETE_WITH_REASON_FORM_BINDING, "errorsDeleteWithReasonForm", "deleteWithReasonForm", DeleteWithReasonForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_REVIEW_FORM_BINDING, "errorsReviewForm", "reviewForm", ReviewForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_NOTE_FORM_BINDING, "errorsEditNoteForm", "editNoteForm", EditNoteForm.class);

        mav.addObject(DELETE_WITH_REASON_REVIEW, model.getOrDefault(DELETE_WITH_REASON_REVIEW, false));
        mav.addObject(DELETE_WITH_REASON_NOTE, model.getOrDefault(DELETE_WITH_REASON_NOTE, false));
        mav.addObject(REVIEW_USER_ID, model.getOrDefault(REVIEW_USER_ID, null));

        Note note = noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new);
        mav.addObject("note", note);
        mav.addObject("reviews", noteService.getReviews(noteId));
        mav.addObject("hierarchy", directoryService.getDirectoryPath(note.getParentId()));
        mav.addObject(NOTE_EDITED, model.getOrDefault(NOTE_EDITED, false));
        mav.addObject(REVIEW_DELETED, model.getOrDefault(REVIEW_DELETED, false));

        return mav;
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.POST)
    public ModelAndView editNote(@PathVariable("noteId") @ValidUuid UUID noteId,
                                 @Valid @ModelAttribute final EditNoteForm editNoteForm,
                                 final BindingResult result, final RedirectAttributes redirectAttributes) {
        final ModelAndView mav = new ModelAndView("redirect:" + editNoteForm.getRedirectUrl());
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(EDIT_NOTE_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(EDIT_NOTE_ID, noteId);
        } else {
            Note note = new Note.NoteBuilder()
                    .id(noteId)
                    .name(editNoteForm.getName())
                    .category(Category.valueOf(editNoteForm.getCategory().toUpperCase()))
                    .visible(editNoteForm.getVisible())
                    .build();
            redirectAttributes.addFlashAttribute(NOTE_EDITED, true);

            noteService.update(note);
        }
        return mav;
    }

    @RequestMapping(value = "/{noteId}/review", method = {RequestMethod.POST})
    public ModelAndView reviewNote(@PathVariable("noteId") @ValidUuid UUID noteId,
                                   @Valid @ModelAttribute final ReviewForm reviewForm,
                                   final BindingResult result, final RedirectAttributes redirectAttributes) {
        final ModelAndView mav = new ModelAndView("redirect:/notes/"+noteId);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_REVIEW_FORM_BINDING, result);
        } else {
            redirectAttributes.addFlashAttribute(REVIEW_UPLOADED, true);
            noteService.createOrUpdateReview(noteId, reviewForm.getScore(), reviewForm.getContent());
        }
        return mav;
    }


    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> getNoteFile(@PathVariable("noteId") @ValidUuid UUID noteId) {
        NoteFile file = noteService.getNoteFileById(noteId).orElseThrow(NoteNotFoundException::new);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getMimeType()))
                .body(file.getContent());

    }

    @RequestMapping(value = "/{noteId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNote(@PathVariable("noteId") @ValidUuid UUID noteId,
                                   @Valid @ModelAttribute("deleteWithReasonForm") final DeleteWithReasonForm deleteWithReasonForm,
                                   final BindingResult result, final RedirectAttributes redirectAttributes) {

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(DELETE_WITH_REASON_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(DELETE_WITH_REASON_NOTE, true);
            return new ModelAndView("redirect:/notes/" + noteId);
        }
        else{
            redirectAttributes.addFlashAttribute(NOTE_DELETED, true);
            noteService.delete(new UUID[]{noteId}, deleteWithReasonForm.getReason());
            return new ModelAndView("redirect:" +  deleteWithReasonForm.getRedirectUrl());
        }

    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
