package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.DirectoryNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.webapp.forms.*;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@Validated
@Controller
@RequestMapping("/directory")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final NoteService noteService;
    private final SearchService searchService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(DirectoryController.class);

    @Autowired
    public DirectoryController(DirectoryService directoryService, NoteService noteService, SearchService searchService) {
        this.directoryService = directoryService;
        this.noteService = noteService;
        this.searchService = searchService;
    }

    @RequestMapping(value = "/{directoryId}" ,method = RequestMethod.GET)
    public ModelAndView getDirectory(@PathVariable("directoryId") String directoryId,
                                     @ModelAttribute("navigationForm") NavigationForm navigationForm,
                                     final ModelMap model) {

        ModelAndView mav = new ModelAndView("directory");

        addFormOrGetWithErrors(mav, model, CREATE_NOTE_FORM_BINDING, "errorsCreateNoteForm", "createNoteForm", CreateNoteForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_NOTE_FORM_BINDING, "errorsEditNoteForm", "editNoteForm", EditNoteForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_DIRECTORY_FORM_BINDING, "errorsCreateDirectoryForm", "createDirectoryForm", CreateDirectoryForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_DIRECTORY_FORM_BINDING, "errorsEditDirectoryForm", "editDirectoryForm", EditDirectoryForm.class);

        mav.addObject("editNoteId", model.get(EDIT_NOTE_ID));
        mav.addObject("editDirectoryId", model.get(EDIT_DIRECTORY_ID));

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
        UUID childId = directoryService.create(createDirectoryForm.getName(), dId, true, "#BBBBBB");
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
            //TODO: add visible attribute
            UUID noteId = noteService.createNote(createNoteForm.getName(), dId, true, createNoteForm.getFile(), createNoteForm.getCategory());
            return new ModelAndView("redirect:/notes/" + noteId);
        }
        catch (IOException e){
            return new ModelAndView("redirect:/directory/" + directoryId); // TODO: Handle errors
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteContent(@RequestParam(required = false) UUID[] directoryIds,
                                      @RequestParam(required = false) UUID[] noteIds,
                                      @RequestParam String redirectUrl) {


        if (noteIds != null && noteIds.length > 0)
            noteService.deleteMany(noteIds);
        if (directoryIds != null && directoryIds.length > 0)
            directoryService.deleteMany(directoryIds);
        return new ModelAndView("redirect:" + redirectUrl);
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST)
    public ModelAndView editDirectory(@PathVariable("directoryId") UUID directoryId,
                                 @Valid @ModelAttribute final EditDirectoryForm editDirectoryForm,
                                 final BindingResult result, final RedirectAttributes redirectAttributes) {

        final ModelAndView mav = new ModelAndView("redirect:" + editDirectoryForm.getRedirectUrl());
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(EDIT_DIRECTORY_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(EDIT_DIRECTORY_ID, directoryId);
        } else {
            Directory directory = new Directory(directoryId, editDirectoryForm.getName(), true, "#BBBBBB");
            directoryService.update(directory);
        }
        return mav;
    }

}

