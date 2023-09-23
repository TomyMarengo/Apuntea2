package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.SearchNotesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final DataService dataService;
    private final NoteService noteService;
    @Autowired
    public SearchController(final DataService dataService, final NoteService noteService) {
        this.dataService = dataService;
        this.noteService = noteService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchNotes(@Valid @ModelAttribute("searchNotesForm") final SearchNotesForm searchNotesForm, final BindingResult result){
        final ModelAndView mav = new ModelAndView("search");

        List<Note> notes = noteService.searchNotes(
                    searchNotesForm.getInstitutionId(),
                    searchNotesForm.getCareerId(),
                    searchNotesForm.getSubjectId(),
                    searchNotesForm.getCategory(),
                    searchNotesForm.getWord(),
                    searchNotesForm.getSortBy(),
                    searchNotesForm.getAscending(),
                    searchNotesForm.getPageNumber(),
                    searchNotesForm.getPageSize()
        );

        mav.addObject("maxPage", 5); //TODO: Change this mocked element
        mav.addObject("notes", notes);
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());
        return mav;
    }
}
