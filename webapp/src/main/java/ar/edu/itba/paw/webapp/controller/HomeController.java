package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
public class HomeController {

    private final DataService dataService;
    private final NoteService noteService;
    // TODO: Ask if this is a good practice
    private static final String CREATE_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.createNoteForm";

    @Autowired
    public HomeController(final DataService dataService, final NoteService noteService) {
        this.dataService = dataService;
        this.noteService = noteService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("searchNotesForm") final SearchNotesForm searchNotesForm,
                              ModelMap model) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());

        if (model.containsAttribute(CREATE_NOTE_FORM_BINDING)) {
            mav.addObject("errors", ((BindingResult) model.get(CREATE_NOTE_FORM_BINDING)).getAllErrors());
        } else {
            mav.addObject("createNoteForm", new CreateNoteForm());
        }
        return mav;
    }
}
