package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.InstitutionData;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.CareerNotFoundException;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.webapp.forms.SearchForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/subjects")
public class SubjectController {

    private final DataService dataService;

    @Autowired
    public SubjectController(final DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView selectCareer(){
        final ModelAndView mav = new ModelAndView("selectCareer");
        InstitutionData institutionData = dataService.getInstitutionData();
        mav.addObject("institutionData", new Gson().toJson(institutionData));
        return mav;
    }

    @RequestMapping(value = "/{careerId}", method = RequestMethod.GET)
    public ModelAndView manageCareer(@PathVariable("careerId") @ValidUuid UUID careerId){
        Career career = dataService.findCareerById(careerId).orElseThrow(CareerNotFoundException::new);
        final ModelAndView mav = new ModelAndView("manageCareer");
        List<Subject> ownedSubjects = dataService.getSubjectsByCareer(careerId);
        mav.addObject("ownedSubjects", ownedSubjects);

//        List<Subject> unownedSubjects = dataService.getSubjectsByInstitution();
//        mav.addObject("unownedSubjects", unownedSubjects);
        return mav;
    }
}
