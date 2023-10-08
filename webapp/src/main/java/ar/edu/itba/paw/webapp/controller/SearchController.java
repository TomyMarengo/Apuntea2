package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.SearchForm;
import com.google.gson.Gson;
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
    private final SecurityService securityService;

    @Autowired
    public SearchController(final DataService dataService, SearchService searchService, SecurityService securityService) {
        this.dataService = dataService;
        this.searchService = searchService;
        this.securityService = securityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult result){
        if (result.hasErrors()) {
            return new ModelAndView("/errors/400");
        }

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

        // TODO: Ask if this should go in the service
        InstitutionData institutionData = dataService.getInstitutionData();
        mav.addObject("institutionData", new Gson().toJson(institutionData));
        return mav;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
