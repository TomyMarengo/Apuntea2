package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final NoteService noteService;
    @Autowired
    public SearchController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchNotes(
            @RequestParam(value = "institution", required = false) final String institution,
            @RequestParam(value = "career", required = false) final String career,
            @RequestParam(value = "subject", required = false) final String subject,
            @RequestParam(value = "category", required = false) final String category,
            @RequestParam(value = "score", required = false) final Float score,
            @RequestParam(value = "sort-by", required = false) final String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") final Boolean ascending,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "page-size", required = false, defaultValue = "10") final Integer pageSize
    ){
        final ModelAndView mav = new ModelAndView("search");
        List<Note> notes = noteService.search(institution, career, subject, category, score, sortBy, ascending, page, pageSize);
        mav.addObject("notes", notes);
        return mav;
    }
}
