package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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


    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET},
    produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") String noteId) {
//        return noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new).getBytes();
        return noteService.getNoteFileById(UUID.fromString(noteId));
    }

}
