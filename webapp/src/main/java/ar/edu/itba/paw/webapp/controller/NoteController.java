package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.CreateNoteForm;
import ar.edu.itba.paw.webapp.forms.ReviewForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final String CREATE_REVIEW_FORM_BINDING = "org.springframework.validation.BindingResult.reviewForm";
    private static final String CREATE_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.createNoteForm";

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
    /*
    @RequestMapping(value = "/{noteId}/review", method = {RequestMethod.POST})
    public ModelAndView reviewNote(@PathVariable("noteId") String noteId,
                                   @Valid @ModelAttribute final ReviewForm reviewForm,
                                   final BindingResult result) {
        if (result.hasErrors()) {
            // TODO: Handle errors
            System.out.println(result.getAllErrors());
        } else {
            noteService.createOrUpdateReview(UUID.fromString(noteId), reviewForm.getUserId(), reviewForm.getScore());
        }
        return new ModelAndView("redirect:/notes/"+noteId);
    }*/

    //TODO: remove when users are implemented
    @RequestMapping(value = "/{noteId}/review", method = {RequestMethod.POST})
    public ModelAndView reviewNoteMail(@PathVariable("noteId") String noteId,
                                   @Valid @ModelAttribute final ReviewForm reviewForm,
                                   final BindingResult result, final RedirectAttributes redirectAttributes) {
        final ModelAndView mav = new ModelAndView("redirect:/notes/"+noteId);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_REVIEW_FORM_BINDING, result);
        } else {
            noteService.createOrUpdateReview(UUID.fromString(noteId), reviewForm.getEmail(), reviewForm.getScore(), reviewForm.getContent());
        }
        return mav;
    }


    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET},
    produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") String noteId) {
//        return noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new).getBytes();
        return noteService.getNoteFileById(UUID.fromString(noteId));
    }

    // TODO: Remove after first sprint
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createNote(@Valid @ModelAttribute final CreateNoteForm createNoteForm,
                                   final BindingResult result,
                                   final RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_NOTE_FORM_BINDING, result);
            return new ModelAndView("redirect:/");
        }

        UUID noteId = noteService.createNote(createNoteForm.getFile(), createNoteForm.getName(), createNoteForm.getEmail(), createNoteForm.getSubjectId(), createNoteForm.getCategory());
        return new ModelAndView("redirect:/notes/" + noteId);
    }

    @RequestMapping(value = "/create/{directoryId}", method = RequestMethod.POST)
    public ModelAndView createNoteWithParent(@Valid @ModelAttribute final CreateNoteForm createNoteForm,
                                  @PathVariable("directoryId") String directoryId,
                                   final BindingResult result,
                                   final RedirectAttributes redirectAttributes)
    {

        UUID parentId = UUID.fromString(directoryId);

        if(result.hasErrors()) {
            return new ModelAndView("redirect:/");
        }

        UUID noteId = noteService.createNote(
                createNoteForm.getFile(),
                createNoteForm.getName(),
                createNoteForm.getEmail(),
                createNoteForm.getSubjectId(),
                createNoteForm.getCategory(),
                parentId
        );
        return new ModelAndView("redirect:/notes/" + noteId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ModelAndView deleteNote(
            @RequestParam("noteId") final String noteId,
            final RedirectAttributes redirectAttributes
    ) {
        UUID nId = UUID.fromString(noteId);
        noteService.delete(nId);
        return new ModelAndView("redirect:/");
    }


}
