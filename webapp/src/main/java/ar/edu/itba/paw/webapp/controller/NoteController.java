package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.CreateNoteForm;
import ar.edu.itba.paw.webapp.forms.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private static final String CREATE_REVIEW_FORM_BINDING = "org.springframework.validation.BindingResult.reviewForm";

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.GET)
    public ModelAndView getNote(@PathVariable("noteId") String noteId, final ModelMap model) {
        final ModelAndView mav = new ModelAndView("note");

        if(model.containsAttribute(CREATE_REVIEW_FORM_BINDING)) {
            mav.addObject("errors", ((BindingResult) model.get(CREATE_REVIEW_FORM_BINDING)).getAllErrors());
        } else {
            mav.addObject("reviewForm", new ReviewForm());
        }
        Note note = noteService.getNoteById(UUID.fromString(noteId)).orElseThrow(NoteNotFoundException::new);
        mav.addObject("note", note);
        mav.addObject("reviews", noteService.getReviews(UUID.fromString(noteId)));
        return mav;
    }

    @RequestMapping(value = "/{noteId}/review", method = {RequestMethod.POST})
    public ModelAndView reviewNote(@PathVariable("noteId") String noteId,
                                   @Valid @ModelAttribute final ReviewForm reviewForm,
                                   final BindingResult result, final RedirectAttributes redirectAttributes) {
        final ModelAndView mav = new ModelAndView("redirect:/notes/"+noteId);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_REVIEW_FORM_BINDING, result);
        } else {
            noteService.createOrUpdateReview(UUID.fromString(noteId), reviewForm.getScore(), reviewForm.getContent());
        }
        return mav;
    }

    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET}, produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") String noteId) {
//        return noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new).getBytes();
        return noteService.getNoteFileById(UUID.fromString(noteId));
    }

    @RequestMapping(value = "/{noteId}/", method = RequestMethod.DELETE)
    public ModelAndView deleteNote(@PathVariable("noteId") String noteId) {
        UUID nId = UUID.fromString(noteId);
        noteService.delete(nId);
        return new ModelAndView("redirect:/");
    }

}
