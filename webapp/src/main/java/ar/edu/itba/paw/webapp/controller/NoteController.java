package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.CreateNoteForm;
import ar.edu.itba.paw.webapp.forms.ReviewForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.UUID;


@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(@Qualifier("noteServiceImpl") final NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.GET)
    public ModelAndView getNote(@PathVariable("noteId") String noteId) {
        final ModelAndView mav = new ModelAndView("note");
//        Note note = noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new);
        mav.addObject("noteId", noteId);
        return mav;
    }
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
    }


    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET},
    produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") String noteId) {
//        return noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new).getBytes();
        return noteService.getNoteFileById(UUID.fromString(noteId));
    }

}
