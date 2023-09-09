package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final NoteService noteService;

    @Autowired
    public HomeController(@Qualifier("noteServiceImpl") final NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/create" ,method = RequestMethod.POST)
    public ModelAndView createNote(
            @RequestParam(value = "name") final String name,
            @RequestParam(value = "file") final MultipartFile file,
            @RequestParam(value = "institution") final String institution,
            @RequestParam(value = "career") final String career,
            @RequestParam(value = "subject") final String subject,
            @RequestParam(value = "category") final String category,
            @RequestParam(value = "email") final String email
    ){ // TODO: Use form
        Note note = noteService.createNote(file, name);
        return new ModelAndView("redirect:/notes/" + note.getNoteId());
    }

}
