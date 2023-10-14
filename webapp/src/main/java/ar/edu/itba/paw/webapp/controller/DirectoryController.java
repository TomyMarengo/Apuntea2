package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.directory.DirectoryNotFoundException;
import ar.edu.itba.paw.models.user.User;
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
import javax.xml.ws.http.HTTPException;
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
        if (result.hasErrors())
            throw new HTTPException(400);

        ModelAndView mav = new ModelAndView("directory");

        loadFormErrors(mav, model);

        mav.addObject("editNoteId", model.get(EDIT_NOTE_ID));
        mav.addObject("editDirectoryId", model.get(EDIT_DIRECTORY_ID));
        loadToastFlashAttributes(mav, model);

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
        mav.addObject("currentPage", pageResult.getCurrentPage());
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
        redirectAttributes.addFlashAttribute(CREATE_DIRECTORY_FORM, true);
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

        UUID noteId = noteService.createNote(createNoteForm.getName(), directoryId, createNoteForm.getVisible(), createNoteForm.getFile(), createNoteForm.getCategory());
        return new ModelAndView("redirect:/notes/" + noteId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteContent(@RequestParam(required = false) UUID[] directoryIds,
                                      @RequestParam(required = false) UUID[] noteIds,
                                      @RequestParam(required = false) @Size(max = 300) String reason,
                                      @RequestParam String redirectUrl,
                                      final RedirectAttributes redirectAttributes) {

        // TODO: Validate redirectUrl?
        if (noteIds != null && noteIds.length > 0){
            noteService.delete(noteIds, reason);
            redirectAttributes.addFlashAttribute(DELETE_NOTE_FORM, true);
        }
        if (directoryIds != null && directoryIds.length > 0) {
            directoryService.delete(directoryIds, reason);
            redirectAttributes.addFlashAttribute(DELETE_DIRECTORY_FORM, true);
        }

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
            redirectAttributes.addFlashAttribute(EDIT_DIRECTORY_FORM, true);
        }
        return mav;
    }

    @RequestMapping(value = "/{directoryId}/addfavorite", method = RequestMethod.POST)
    public ModelAndView addFavoriteDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                          @RequestParam String redirectUrl, final RedirectAttributes redirectAttributes) {
        // TODO: Validate redirectUrl?
        directoryService.addFavorite(directoryId);
        redirectAttributes.addFlashAttribute(ADD_FAVORITE, true);
        // TODO: display a message saying that the directory was added to favorites
        return new ModelAndView("redirect:" + redirectUrl);
    }

    @RequestMapping(value = "/{directoryId}/removefavorite", method = RequestMethod.POST)
    public ModelAndView removeFavoriteDirectory(@PathVariable("directoryId") @ValidUuid UUID directoryId,
                                          @RequestParam String redirectUrl, final RedirectAttributes redirectAttributes) {
        // TODO: Validate redirectUrl?
        directoryService.removeFavorite(directoryId);
        redirectAttributes.addFlashAttribute(REMOVE_FAVORITE, true);
        // TODO: display a message saying that the directory was removed from favorites
        // If removeFavorite returns something different than 1, handle the error
        return new ModelAndView("redirect:" + redirectUrl);
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        return this.securityService.getCurrentUser().orElse(null);
    }

    private void loadToastFlashAttributes(ModelAndView mav, ModelMap model){
        mav.addObject(CREATE_DIRECTORY_FORM, model.getOrDefault(CREATE_DIRECTORY_FORM, false));
        mav.addObject(DELETE_DIRECTORY_FORM, model.getOrDefault(DELETE_DIRECTORY_FORM, false));
        mav.addObject(EDIT_DIRECTORY_FORM, model.getOrDefault(EDIT_DIRECTORY_FORM, false));
        mav.addObject(DELETE_NOTE_FORM, model.getOrDefault(DELETE_NOTE_FORM, false));
        mav.addObject(ADD_FAVORITE, model.getOrDefault(ADD_FAVORITE, false));
        mav.addObject(REMOVE_FAVORITE, model.getOrDefault(REMOVE_FAVORITE, false));

    }

    private void loadFormErrors(ModelAndView mav, ModelMap model){
        addFormOrGetWithErrors(mav, model, CREATE_NOTE_FORM_BINDING, "errorsCreateNoteForm", "createNoteForm", CreateNoteForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_NOTE_FORM_BINDING, "errorsEditNoteForm", "editNoteForm", EditNoteForm.class);
        addFormOrGetWithErrors(mav, model, CREATE_DIRECTORY_FORM_BINDING, "errorsCreateDirectoryForm", "createDirectoryForm", CreateDirectoryForm.class);
        addFormOrGetWithErrors(mav, model, EDIT_DIRECTORY_FORM_BINDING, "errorsEditDirectoryForm", "editDirectoryForm", EditDirectoryForm.class);

    }
}


