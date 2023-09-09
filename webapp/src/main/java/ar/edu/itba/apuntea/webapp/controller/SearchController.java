package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.DataService;
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
    private final DataService dataService;
    @Autowired
    public SearchController(final DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchNotes(
            // TODO: Add validation
            @RequestParam(value = "institution", required = false) final String institution,
            @RequestParam(value = "career", required = false) final String career,
            @RequestParam(value = "subject", required = false) final String subject,
            @RequestParam(value = "category", required = false) final String category,
            @RequestParam(value = "score", required = false) final Float score,
            @RequestParam(value = "sort-by", required = false, defaultValue = "score") final String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") final Boolean ascending,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page,
            @RequestParam(value = "page-size", required = false, defaultValue = "10") final Integer pageSize
    ){
        final ModelAndView mav = new ModelAndView("search");

        List<Note> notes = dataService.searchNotes(institution, career, subject, category, score, sortBy, ascending, page, pageSize);
        mav.addObject("notes", notes);
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());
        return mav;
    }
}
