package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.note.NoteNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.EditNoteForm;
import ar.edu.itba.paw.webapp.forms.ReviewForm;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

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
import javax.validation.constraints.Size;
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
        mav.addObject(EDIT_NOTE_FORM, model.getOrDefault(EDIT_NOTE_FORM, false));
        mav.addObject(DELETE_REVIEW, model.getOrDefault(DELETE_REVIEW, false));

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
                    .noteId(noteId)
                    .name(editNoteForm.getName())
                    .category(Category.valueOf(editNoteForm.getCategory().toUpperCase()))
                    .visible(editNoteForm.getVisible())
                    .build();
            redirectAttributes.addFlashAttribute(EDIT_NOTE_FORM, true);
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
            redirectAttributes.addFlashAttribute(UPLOAD_REVIEW_FORM, true);
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
                                   @RequestParam(required = false) @Size(max = 300) String reason,
                                   @RequestParam(required = false, defaultValue = "/") String redirectUrl
                                ) {
        noteService.delete(new UUID[]{noteId}, reason);
        return new ModelAndView("redirect:"  + redirectUrl);
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
