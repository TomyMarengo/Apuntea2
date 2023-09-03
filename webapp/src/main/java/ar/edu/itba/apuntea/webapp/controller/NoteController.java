package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView create(){
        return new ModelAndView("create");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView createNote(
            @RequestParam(value = "file", required = true) final MultipartFile file,
            @RequestParam(value = "institution", required = true) final String institution,
            @RequestParam(value = "career", required = true) final String career,
            @RequestParam(value = "subject", required = true) final String subject,
            @RequestParam(value = "type", required = true) final String type
    ){
        Note note = noteService.create(file, institution, career, subject, type);
        return new ModelAndView("search"); //TODO: change for 'Has subido correctamente el archivo, etc'
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        return new ModelAndView("search");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchNotes(
            @RequestParam(value = "institution", required = false) final String institution,
            @RequestParam(value = "career", required = false) final String career,
            @RequestParam(value = "subject", required = false) final String subject,
            @RequestParam(value = "type", required = false) final String type,
            @RequestParam(value = "score", required = false) final float score
    ){
        final ModelAndView mav = new ModelAndView("search");
        List<Note> notes = noteService.search(institution, career, subject, type, score);
        mav.addObject("notes", notes);
        return mav;
    }

    @RequestMapping( value = "/{noteId}/download", method = {RequestMethod.GET},
    produces = {"application/pdf"} ) // TODO: change
    @ResponseBody
    public byte[] getNoteFile(@PathVariable("noteId") String noteId) {
//        return noteService.getNoteById(noteId).orElseThrow(NoteNotFoundException::new).getBytes();
        return noteService.getNoteFileById(new UUID(1234567890, 1234567890));
    }
}
