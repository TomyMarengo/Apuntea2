package ar.edu.itba.apuntea.webapp.controller;

import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.services.CareerService;
import ar.edu.itba.apuntea.services.InstitutionService;
import ar.edu.itba.apuntea.services.NoteService;
import ar.edu.itba.apuntea.services.SubjectService;
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
    private final InstitutionService institutionService;
    private final CareerService careerService;
    private final SubjectService subjectService;
    @Autowired
    public SearchController(final NoteService noteService, final InstitutionService institutionService, final CareerService careerService, final SubjectService subjectService) {
        this.noteService = noteService;
        this.institutionService = institutionService;
        this.careerService = careerService;
        this.subjectService = subjectService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchNotes(
            // TODO: Add validation
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
        mav.addObject("institutions", institutionService.getInstitutions());
        mav.addObject("careers", careerService.getCareers());
        mav.addObject("subjects", subjectService.getSubjects());
        return mav;
    }
}
