package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.exceptions.DirectoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.NoteNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.DataService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.CreateDirectoryForm;
import ar.edu.itba.paw.webapp.forms.CreateNoteForm;
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

    @Autowired
    public DirectoryController(DirectoryService directoryService, NoteService noteService, DataService dataService, SearchService searchService) {
        this.directoryService = directoryService;
        this.noteService = noteService;
        this.dataService = dataService;
        this.searchService = searchService;
    }

    @RequestMapping(value = "/{directoryId}" ,method = RequestMethod.GET)
    public ModelAndView getDirectory(@PathVariable("directoryId") String directoryId,
                                     final ModelMap model) {

        ModelAndView mav = new ModelAndView("directory");

        if(model.containsAttribute(CREATE_NOTE_FORM_BINDING)) {
            mav.addObject("errorsNoteForm", ((BindingResult) model.get(CREATE_NOTE_FORM_BINDING)).getAllErrors());
        } else {
            mav.addObject("createNoteForm", new CreateNoteForm());
        }

        if(model.containsAttribute(CREATE_DIRECTORY_FORM_BINDING)) {
            mav.addObject("errorsDirectoryForm", ((BindingResult) model.get(CREATE_DIRECTORY_FORM_BINDING)).getAllErrors());
        } else {
            mav.addObject("createDirectoryForm", new CreateDirectoryForm());
        }

        mav.addObject("institutions", dataService.getInstitutions());
        mav.addObject("careers", dataService.getCareers());
        mav.addObject("subjects", dataService.getSubjects());


        UUID dId = UUID.fromString(directoryId);
        Directory directory = directoryService.getDirectoryById(dId).orElseThrow(DirectoryNotFoundException::new);

        mav.addObject("directory", directory);

        /*
        mav.addObject("childrenDirectories", directoryService.getChildren(dId));
        mav.addObject("childrenNotes", noteService.getNotesByParentDirectory(dId));*/

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
            redirectAttributes.addFlashAttribute(CREATE_NOTE_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory" + directoryId);
        }

        UUID childId = directoryService.create(createDirectoryForm.getName(), createDirectoryForm.getParentId());
        return new ModelAndView("redirect:/directory/" + childId  + "/");
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST, params = "createNote")
    public ModelAndView addNote(@PathVariable("directoryId") String directoryId,
                                @Valid @ModelAttribute final CreateNoteForm createNoteForm,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes)
    {

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_NOTE_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory/" + directoryId + "/");
        }

        try {
            UUID noteId = noteService.createNote(createNoteForm.getFile(), createNoteForm.getName(), createNoteForm.getSubjectId(),
                    createNoteForm.getCategory(), createNoteForm.getParentId());
            return new ModelAndView("redirect:/notes/" + noteId + "/");
        }
        catch (IOException e){
            return new ModelAndView("redirect:/directory/" + directoryId + "/"); // TODO: Handle errors
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
