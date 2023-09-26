package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.models.exceptions.DirectoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.CreateDirectoryForm;
import ar.edu.itba.paw.webapp.forms.CreateNoteForm;
import ar.edu.itba.paw.webapp.forms.NavigationForm;
import ar.edu.itba.paw.webapp.forms.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/directory")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final NoteService noteService;
    private final DataService dataService;
    private final SearchService searchService;

    private static final String CREATE_NOTE_FORM_BINDING = "org.springframework.validation.BindingResult.createNoteForm";
    private static final String CREATE_DIRECTORY_FORM_BINDING = "org.springframework.validation.BindingResult.createDirectoryForm";

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(DirectoryController.class);

    @Autowired
    public DirectoryController(DirectoryService directoryService, NoteService noteService, DataService dataService, SearchService searchService) {
        this.directoryService = directoryService;
        this.noteService = noteService;
        this.dataService = dataService;
        this.searchService = searchService;
    }

    // TODO: Move to utils class
    private void addFormOrGetWithErrors(ModelAndView mav, ModelMap model, String attribute, String errorName, String formName, Class<?> form) {
        if(model.containsAttribute(attribute)) {
            mav.addObject(errorName, ((BindingResult) model.get(attribute)).getAllErrors());
        } else {
            try {
                mav.addObject(formName, form.newInstance());
            } catch (InstantiationException ex) {
                LOGGER.error("Instantiation exception creating form {}", formName);
            } catch (IllegalAccessException ex) {
                LOGGER.error("Illegal access exception creating form {}", formName);
            }
        }
    }

    @RequestMapping(value = "/{directoryId}" ,method = RequestMethod.GET)
    public ModelAndView getDirectory(@PathVariable("directoryId") String directoryId,
                                     @ModelAttribute("navigationForm") NavigationForm navigationForm,
                                     final ModelMap model) {

        ModelAndView mav = new ModelAndView("directory");

        addFormOrGetWithErrors(mav, model, CREATE_NOTE_FORM_BINDING, "errorsNoteForm", "createNoteForm", CreateNoteForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_DIRECTORY_FORM_BINDING, "errorsDirectoryForm", "createDirectoryForm", CreateDirectoryForm.class);

        UUID dId = UUID.fromString(directoryId);
        Directory directory = directoryService.getDirectoryById(dId).orElseThrow(DirectoryNotFoundException::new);

        Page<Searchable> pageResult = searchService.getNavigationResults(
                dId,
                navigationForm.getCategory(),
                navigationForm.getWord(),
                navigationForm.getSortBy(),
                navigationForm.getAscending(),
                navigationForm.getPageNumber(),
                navigationForm.getPageSize()
        );

        mav.addObject("maxPage", pageResult.getTotalPages());
        mav.addObject("results", pageResult.getContent());
        mav.addObject("directory", directory);

        return mav;
    }


    // Create a POST function with createDirectoryForm and createNoteForm, only one of them will be filled
    // If createDirectoryForm is filled, create a directory with the name and parent directory
    // If createNoteForm is filled, create a note with the file, name, subjectId, category and parent directory
    // If there is an error, redirect to /{directoryId} with the error
    // If there is no error, redirect to /notes/{noteId} or /directory/{directoryId} depending on which form was filled
    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST, params = "createDirectory")
    public ModelAndView addDirectory(@PathVariable("directoryId") String directoryId,
                                     @Valid @ModelAttribute final CreateDirectoryForm createDirectoryForm,
                                     final BindingResult result,
                                     final RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_DIRECTORY_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory/" + directoryId);
        }

        UUID dId = UUID.fromString(directoryId);
        UUID childId = directoryService.create(createDirectoryForm.getName(), dId);
        return new ModelAndView("redirect:/directory/" + dId);
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST, params = "createNote")
    public ModelAndView addNote(@PathVariable("directoryId") String directoryId,
                                @Valid @ModelAttribute final CreateNoteForm createNoteForm,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes)
    {

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_NOTE_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory/" + directoryId);
        }

        UUID dId = UUID.fromString(directoryId);
        try {
            UUID noteId = noteService.createNote(createNoteForm.getFile(), createNoteForm.getName(), createNoteForm.getCategory(), dId);
            return new ModelAndView("redirect:/notes/" + noteId);
        }
        catch (IOException e){
            return new ModelAndView("redirect:/directory/" + directoryId); // TODO: Handle errors
        }
    }

    @RequestMapping(value = "/{directoryId}/deletes", method = RequestMethod.POST)
    public ModelAndView deleteContent(@PathVariable("directoryId") String directoryId) {
        UUID dId = UUID.fromString(directoryId);
        directoryService.delete(dId); //TODO: deleteMany
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/{directoryId}/", method = RequestMethod.DELETE)
    public ModelAndView deleteDirectory(@PathVariable("directoryId") String directoryId) {
        UUID dId = UUID.fromString(directoryId);
        directoryService.delete(dId);
        return new ModelAndView("redirect:/");
    }
}
