package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.institutional.dtos.InstitutionDataDto;
import ar.edu.itba.paw.models.search.Searchable;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.services.InstitutionService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.forms.search.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;


@Controller
@RequestMapping("/search")
public class SearchController {
    private final InstitutionService institutionService;
    private final SearchService searchService;
    private final SecurityService securityService;
    private final UserService userService;

    @Autowired
    public SearchController(final InstitutionService institutionService, final SearchService searchService, final SecurityService securityService, final UserService userService) {
        this.institutionService = institutionService;
        this.searchService = searchService;
        this.securityService = securityService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult result, final ModelMap model){
        if (result.hasErrors())
            throw new HTTPException(400);

        final ModelAndView mav = new ModelAndView("search");

        if (searchForm.getUserId() != null)
            userService.findById(searchForm.getUserId()).ifPresent(u -> mav.addObject("filterUser", u));

        Page<Searchable> pageResult = searchService.search(
                    searchForm.getInstitutionId(),
                    searchForm.getCareerId(),
                    searchForm.getSubjectId(),
                    searchForm.getUserId(),
                    searchForm.getNormalizedCategory(),
                    searchForm.getWord(),
                    searchForm.getSortBy(),
                    searchForm.getAscending(),
                    searchForm.getPageNumber(),
                    searchForm.getPageSize()
        );

        mav.addObject("maxPage", pageResult.getTotalPages());
        mav.addObject("currentPage", pageResult.getCurrentPage());
        mav.addObject("results", pageResult.getContent());
        mav.addObject(FAVORITE_ADDED, model.getOrDefault(FAVORITE_ADDED, false));
        mav.addObject(FAVORITE_REMOVED, model.getOrDefault(FAVORITE_REMOVED, false));
        InstitutionDataDto institutionDataDto = institutionService.getInstitutionData();

        String data = toSafeJson(institutionDataDto);
        mav.addObject("institutionData", data);
        return mav;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
