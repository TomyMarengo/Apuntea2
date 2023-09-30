package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.EditNoteForm;
import ar.edu.itba.paw.webapp.forms.ReviewForm;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
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

        addFormOrGetWithErrors(mav, model, CREATE_REVIEW_FORM_BINDING, "errorsReviewForm", "reviewForm", ReviewForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_NOTE_FORM_BINDING, "errorsEditNoteForm", "editNoteForm", EditNoteForm.class);

        Note note = noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new);
        mav.addObject("note", note);
        mav.addObject("reviews", noteService.getReviews(noteId));
        mav.addObject("hierarchy", directoryService.getDirectoryPath(note.getParentId()));

        User user = securityService.getCurrentUser().orElse(null);
        mav.addObject("user", user);
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
            Note note = new Note(noteId, editNoteForm.getName(), Category.valueOf(editNoteForm.getCategory().toUpperCase()), editNoteForm.getVisible());
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
            noteService.createOrUpdateReview(noteId, reviewForm.getScore(), reviewForm.getContent());
        }
        return mav;
    }

    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET}, produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") @ValidUuid UUID noteId) {
        return noteService.getNoteFileById(noteId).orElseThrow(NoteNotFoundException::new);
    }

    @RequestMapping(value = "/{noteId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNote(@PathVariable("noteId") @ValidUuid UUID noteId) {
        noteService.delete(noteId);
        return new ModelAndView("redirect:/");
    }

}
