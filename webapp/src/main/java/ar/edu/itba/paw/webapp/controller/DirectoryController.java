package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.DirectoryNotFoundException;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.services.SearchService;
import ar.edu.itba.paw.services.SecurityService;
import ar.edu.itba.paw.webapp.forms.*;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.*;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
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
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.UUID;

@Validated
@Controller
@RequestMapping("/directory")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final NoteService noteService;
    private final SearchService searchService;
    private final SecurityService securityService;

    private	static	final Logger LOGGER	= LoggerFactory.getLogger(DirectoryController.class);

    @Autowired
    public DirectoryController(DirectoryService directoryService, NoteService noteService, SearchService searchService, SecurityService securityService) {
        this.directoryService = directoryService;
        this.noteService = noteService;
        this.searchService = searchService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/{directoryId}" ,method = RequestMethod.GET)
    public ModelAndView getDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                     @Valid @ModelAttribute("navigationForm") NavigationForm navigationForm, final BindingResult result,
                                     final ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("/errors/400");
        }

        ModelAndView mav = new ModelAndView("directory");

        addFormOrGetWithErrors(mav, model, CREATE_NOTE_FORM_BINDING, "errorsCreateNoteForm", "createNoteForm", CreateNoteForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_NOTE_FORM_BINDING, "errorsEditNoteForm", "editNoteForm", EditNoteForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_DIRECTORY_FORM_BINDING, "errorsCreateDirectoryForm", "createDirectoryForm", CreateDirectoryForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_DIRECTORY_FORM_BINDING, "errorsEditDirectoryForm", "editDirectoryForm", EditDirectoryForm.class);

        mav.addObject("editNoteId", model.get(EDIT_NOTE_ID));
        mav.addObject("editDirectoryId", model.get(EDIT_DIRECTORY_ID));

        Directory directory = directoryService.getDirectoryById(directoryId).orElseThrow(DirectoryNotFoundException::new);

        Page<Searchable> pageResult = searchService.getNavigationResults(
                directoryId,
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

        mav.addObject("hierarchy", directoryService.getDirectoryPath(directoryId));

        User user = securityService.getCurrentUser().orElse(null);
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST, params = "createDirectory")
    public ModelAndView addDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                     @Valid @ModelAttribute final CreateDirectoryForm createDirectoryForm,
                                     final BindingResult result,
                                     final RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_DIRECTORY_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory/" + directoryId);
        }

        UUID childId = directoryService.create(createDirectoryForm.getName(), directoryId, createDirectoryForm.getVisible(), createDirectoryForm.getColor());
        return new ModelAndView("redirect:/directory/" + directoryId);
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST, params = "createNote")
    public ModelAndView addNote(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                @Valid @ModelAttribute final CreateNoteForm createNoteForm,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes)
    {

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute(CREATE_NOTE_FORM_BINDING, result);
            return new ModelAndView("redirect:/directory/" + directoryId);
        }

        try {
            UUID noteId = noteService.createNote(createNoteForm.getName(), directoryId, createNoteForm.getVisible(), createNoteForm.getFile(), createNoteForm.getCategory());
            return new ModelAndView("redirect:/notes/" + noteId);
        }
        catch (IOException e){
            return new ModelAndView("redirect:/errors/500");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteContent(@RequestParam(required = false) UUID[] directoryIds,
                                      @RequestParam(required = false) UUID[] noteIds,
                                      @RequestParam(required = false) @Size(max = 300) String reason,
                                      @RequestParam String redirectUrl) {

        // TODO: Validate redirectUrl?
        if (noteIds != null && noteIds.length > 0)
            noteService.delete(noteIds, reason);
        if (directoryIds != null && directoryIds.length > 0)
            directoryService.delete(directoryIds, reason);
        return new ModelAndView("redirect:" + redirectUrl);
    }

    @RequestMapping(value = "/{directoryId}", method = RequestMethod.POST)
    public ModelAndView editDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                 @Valid @ModelAttribute final EditDirectoryForm editDirectoryForm,
                                 final BindingResult result, final RedirectAttributes redirectAttributes) {

        final ModelAndView mav = new ModelAndView("redirect:" + editDirectoryForm.getRedirectUrl());
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(EDIT_DIRECTORY_FORM_BINDING, result);
            redirectAttributes.addFlashAttribute(EDIT_DIRECTORY_ID, directoryId);
        } else {
            Directory directory = new Directory(directoryId, editDirectoryForm.getName(), editDirectoryForm.getVisible(), editDirectoryForm.getColor());
            directoryService.update(directory);
        }
        return mav;
    }

    @RequestMapping(value = "/{directoryId}/addfavorite", method = RequestMethod.POST)
    public ModelAndView addFavoriteDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                          @RequestParam @ValidUuid UUID parentId) {
        directoryService.addFavorite(directoryId);
        // TODO: display a message saying that the directory was added to favorites
        return new ModelAndView("redirect:/directory/" + parentId);
    }

    @RequestMapping(value = "/{directoryId}/removefavorite", method = RequestMethod.POST)
    public ModelAndView removeFavoriteDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                          @RequestParam @ValidUuid UUID parentId) {
        directoryService.removeFavorite(directoryId);
        // TODO: display a message saying that the directory was removed from favorites
        // If removeFavorite returns something different than 1, handle the error
        return new ModelAndView("redirect:/directory/" + parentId);
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }

}

