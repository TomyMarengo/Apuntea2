package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.dtos.InstitutionDataDto;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.institutional.CareerNotFoundException;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.forms.institutional.LinkSubjectForm;
import ar.edu.itba.paw.webapp.forms.institutional.CreateSubjectForm;
import ar.edu.itba.paw.webapp.forms.institutional.EditSubjectForm;
import ar.edu.itba.paw.webapp.forms.institutional.UnlinkSubjectForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

@Controller
@RequestMapping("/manage/careers")
public class ManageCareersController {

    private final InstitutionService institutionService;
    private final CareerService careerService;
    private final SubjectService subjectService;
    private final SecurityService securityService;

    @Autowired
    public ManageCareersController(final InstitutionService institutionService, final CareerService careerService, final SubjectService subjectService, SecurityService securityService) {
        this.securityService = securityService;
        this.institutionService = institutionService;
        this.subjectService = subjectService;
        this.careerService = careerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView manageCareers (){
        final ModelAndView mav = new ModelAndView("manage-careers");
        InstitutionDataDto institutionDataDto = institutionService.getInstitutionData();
        mav.addObject("institutionData", toSafeJson(institutionDataDto));
        return mav;
    }

    @RequestMapping(value = "{careerId}", method = RequestMethod.GET)
    public ModelAndView listCareer(@PathVariable("careerId") @ValidUuid UUID careerId, final ModelMap model){
        final ModelAndView mav = new ModelAndView("manage-careers");

        addFormOrGetWithErrors(mav, model, LINK_SUBJECT_FORM_BINDING, "errorsLinkSubjectForm", "linkSubjectForm", LinkSubjectForm.class);
        addFormOrGetWithErrors(mav, model, UNLINK_SUBJECT_FORM_BINDING, "errorsUnlinkSubjectForm", "unlinkSubjectForm", UnlinkSubjectForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_SUBJECT_FORM_BINDING, "errorsCreateSubjectForm", "createSubjectForm", CreateSubjectForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_SUBJECT_FORM_BINDING, "errorsEditSubjectForm", "editSubjectForm", EditSubjectForm.class);

        mav.addObject(SUBJECT_LINKED, model.getOrDefault(SUBJECT_LINKED, false));
        mav.addObject(SUBJECT_UNLINKED, model.getOrDefault(SUBJECT_UNLINKED, false));
        mav.addObject(SUBJECT_CREATED, model.getOrDefault(SUBJECT_CREATED, false));
        mav.addObject(SUBJECT_EDITED, model.getOrDefault(SUBJECT_EDITED, false));

        InstitutionDataDto institutionDataDto = institutionService.getInstitutionData();
        mav.addObject("institutionData", toSafeJson(institutionDataDto));

        Career career = careerService.getCareerById(careerId).orElseThrow(CareerNotFoundException::new);
        mav.addObject("career", career);

        List<Subject> ownedSubjects = subjectService.getSubjectsByCareer(careerId);
        mav.addObject("ownedSubjects", ownedSubjects);

        List<Subject> unownedSubjects = subjectService.getSubjectsByCareerComplemented(careerId);
        mav.addObject("unownedSubjects", toSafeJson(unownedSubjects));
        return mav;
    }


    @RequestMapping(value = "/{careerId}/linkSubject", method = RequestMethod.POST)
    public ModelAndView linkSubject(@PathVariable("careerId") @ValidUuid UUID careerId,
                                   @Valid @ModelAttribute final LinkSubjectForm linkSubjectForm,
                                   final BindingResult result,
                                   final RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(LINK_SUBJECT_FORM_BINDING, result);
            return new ModelAndView("redirect:/manage/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/manage/careers/"+careerId);
        subjectService.linkSubjectToCareer(linkSubjectForm.getSubjectId(), careerId, linkSubjectForm.getYear());
        redirectAttributes.addFlashAttribute(SUBJECT_LINKED, true);
        return mav;
    }

    @RequestMapping(value = "/{careerId}/unlinkSubject", method = RequestMethod.POST)
    public ModelAndView unlinkSubject(@PathVariable("careerId") @ValidUuid UUID careerId,
                                      @Valid @ModelAttribute final UnlinkSubjectForm unlinkSubjectForm,
                                     final BindingResult result,
                                     final RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(UNLINK_SUBJECT_FORM_BINDING, result);
            return new ModelAndView("redirect:/manage/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/manage/careers/"+careerId);
        subjectService.unlinkSubjectFromCareer(unlinkSubjectForm.getSubjectId(), careerId);
        redirectAttributes.addFlashAttribute(SUBJECT_UNLINKED, true);
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
            return new ModelAndView("redirect:/manage/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/manage/careers/"+careerId);
        subjectService.createSubject(createSubjectForm.getName(), careerId, createSubjectForm.getYear());
        redirectAttributes.addFlashAttribute(SUBJECT_CREATED, true);
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
            return new ModelAndView("redirect:/manage/careers/"+careerId);
        }
        final ModelAndView mav = new ModelAndView("redirect:/manage/careers/"+careerId);
        subjectService.updateSubjectCareer(editSubjectForm.getSubjectId(), editSubjectForm.getName(), careerId, editSubjectForm.getYear());
        redirectAttributes.addFlashAttribute(SUBJECT_EDITED, true);
        return mav;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }
}
