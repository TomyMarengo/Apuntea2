package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(){
        return new ModelAndView("create");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createNote(
            @RequestParam(value = "file", required = true) final MultipartFile file,
            @RequestParam(value = "university", required = true) final String university,
            @RequestParam(value = "career", required = true) final String career,
            @RequestParam(value = "subject", required = true) final String subject,
            @RequestParam(value = "type", required = true) final String type
    ){
        Note note = noteService.create(file, university, career, subject, type);
        return new ModelAndView("search"); //TODO: change for 'Has subido correctamente el archivo, etc'
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        return new ModelAndView("search");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchNotes(
            @RequestParam(value = "university", required = false) final String university,
            @RequestParam(value = "career", required = false) final String career,
            @RequestParam(value = "subject", required = false) final String subject,
            @RequestParam(value = "type", required = false) final String type,
            @RequestParam(value = "score", required = false) final float score
    ){
        final ModelAndView mav = new ModelAndView("search");
        List<Note> notes = noteService.search(university, career, subject, type, score);
        mav.addObject("notes", notes);
        return mav;
    }

}
