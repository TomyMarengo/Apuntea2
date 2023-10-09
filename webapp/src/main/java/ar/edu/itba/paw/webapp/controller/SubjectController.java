package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.InstitutionData;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.CareerNotFoundException;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.AddSubjectForm;
import ar.edu.itba.paw.webapp.forms.CreateSubjectForm;
import ar.edu.itba.paw.webapp.forms.EditSubjectForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

@Controller
@RequestMapping("/careers")
public class SubjectController {

    private final DataService dataService;
    private final SecurityService securityService;

    @Autowired
    public SubjectController(final DataService dataService, final SecurityService securityService) {
        this.dataService = dataService;
        this.securityService = securityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView selectCareer(){
        final ModelAndView mav = new ModelAndView("manageCareer");
        InstitutionData institutionData = dataService.getInstitutionData();
        mav.addObject("institutionData", new Gson().toJson(institutionData));
        return mav;
    }

    @RequestMapping(value = "/{careerId}", method = RequestMethod.GET)
    public ModelAndView manageCareer(@PathVariable("careerId") @ValidUuid UUID careerId, final ModelMap model){
        final ModelAndView mav = new ModelAndView("manageCareer");

        addFormOrGetWithErrors(mav, model, ADD_SUBJECT_FORM_BINDING, "errorsAddSubjectForm", "addSubjectForm", AddSubjectForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_SUBJECT_FORM_BINDING, "errorsCreateSubjectForm", "createSubjectForm", CreateSubjectForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_SUBJECT_FORM_BINDING, "errorsEditSubjectForm", "editSubjectForm", EditSubjectForm.class);
        

        InstitutionData institutionData = dataService.getInstitutionData();
        mav.addObject("institutionData", new Gson().toJson(institutionData));

        Career career = dataService.findCareerById(careerId).orElseThrow(CareerNotFoundException::new);
        mav.addObject("career", career);

        List<Subject> ownedSubjects = dataService.getSubjectsByCareer(careerId);
        mav.addObject("ownedSubjects", ownedSubjects);

        List<Subject> unownedSubjects = dataService.getSubjectsByCareerComplemented(careerId);
        mav.addObject("unownedSubjects", new  Gson().toJson(unownedSubjects));
        return mav;
    }


    @RequestMapping(value = "/{careerId}/addSubject", method = RequestMethod.POST)
    public ModelAndView addSubject(@PathVariable("careerId") @ValidUuid UUID careerId,
                                   @Valid @ModelAttribute final AddSubjectForm addSubjectForm,
                                   final BindingResult result,
                                   final RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ADD_SUBJECT_FORM_BINDING, result);
            return new ModelAndView("redirect:/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/careers/"+careerId);
        dataService.addSubjectToCareer(addSubjectForm.getSubjectId(), careerId, addSubjectForm.getYear());
        return mav;
    }

    @RequestMapping(value = "/{careerId}/createSubject", method = RequestMethod.POST)
    public ModelAndView createSubject(@PathVariable("careerId") @ValidUuid UUID careerId,
                                    @Valid @ModelAttribute final CreateSubjectForm createSubjectForm,
                                    final BindingResult result,
                                    final RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_SUBJECT_FORM_BINDING, result);
            return new ModelAndView("redirect:/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/careers/"+careerId);
        dataService.createSubject(createSubjectForm.getName(), careerId, createSubjectForm.getYear());
        return mav;
    }

    @RequestMapping(value = "/{careerId}/editSubject", method = RequestMethod.POST)
    public ModelAndView editSubject(@PathVariable("careerId") @ValidUuid UUID careerId,
                                    @Valid @ModelAttribute final EditSubjectForm editSubjectForm,
                                    final BindingResult result,
                                    final RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(EDIT_SUBJECT_FORM_BINDING, result);
            return new ModelAndView("redirect:/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/careers/"+careerId);
        dataService.updateSubjectCareer(editSubjectForm.getSubjectId(), editSubjectForm.getName(), careerId, editSubjectForm.getYear());
        return mav;
    }



    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
