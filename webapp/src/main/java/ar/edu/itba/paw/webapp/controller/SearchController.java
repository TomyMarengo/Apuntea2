package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final DataService dataService;
    private final SearchService searchService;

    @Autowired
    public SearchController(final DataService dataService, SearchService searchService) {
        this.dataService = dataService;
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult result){
        final ModelAndView mav = new ModelAndView("search");

        Page<Searchable> pageResult = searchService.search(
                    searchForm.getInstitutionId(),
                    searchForm.getCareerId(),
                    searchForm.getSubjectId(),
                    searchForm.getCategory(),
                    searchForm.getWord(),
                    searchForm.getSortBy(),
                    searchForm.getAscending(),
                    searchForm.getPageNumber(),
                    searchForm.getPageSize()
        );

        mav.addObject("maxPage", pageResult.getTotalPages());
        mav.addObject("results", pageResult.getContent());
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());
        return mav;
    }
}
