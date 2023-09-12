package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.services.DataService;
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
    @Autowired
    public SearchController(final DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView searchNotes(@Valid @ModelAttribute("searchNotesForm") final SearchNotesForm searchNotesForm, final BindingResult result){
        final ModelAndView mav = new ModelAndView("search");

        List<Note> notes;
        if (searchNotesForm.getWord() == null || searchNotesForm.getWord().isEmpty()) {
            notes = dataService.searchNotes(
                    searchNotesForm.getInstitutionId(),
                    searchNotesForm.getCareerId(),
                    searchNotesForm.getSubjectId(),
                    searchNotesForm.getCategory(),
                    searchNotesForm.getScore(),
                    searchNotesForm.getSortBy(),
                    searchNotesForm.getAscending(),
                    searchNotesForm.getPage(),
                    searchNotesForm.getPageSize()
            );
        }
        else {
            notes = dataService.searchNotesByWord(searchNotesForm.getWord());
        }
        
        mav.addObject("notes", notes);
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());
        return mav;
    }
}
