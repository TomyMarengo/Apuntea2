package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.services.DirectoryService;
import ar.edu.itba.paw.services.NoteService;
import ar.edu.itba.paw.webapp.forms.CreateDirectoryForm;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/directory")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final NoteService noteService;

    @Autowired
    public DirectoryController(DirectoryService directoryService, NoteService noteService) {
        this.directoryService = directoryService;
        this.noteService = noteService;
    }

    @RequestMapping(value = "/{directoryId}" ,method = RequestMethod.GET)
    public ModelAndView getDirectory(@PathVariable("directoryId") String directoryId) {
        UUID dId = UUID.fromString(directoryId);
        DirectoryPath directoryPath = directoryService.getDirectoryPath(dId);
        ModelAndView mav = new ModelAndView("directory");
        mav.addObject("currentDirectory", directoryPath.getCurrentDirectory());
        mav.addObject("parentDirectory", directoryPath.getParentDirectory());
        mav.addObject("rootDirectory", directoryPath.getRootDirectory());

        mav.addObject("childrenDirectories", directoryService.getChildren(dId));
        mav.addObject("childrenNotes", noteService.getNotesByParentDirectory(dId));
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createDirectory(@ModelAttribute("createDirectoryForm") final CreateDirectoryForm createDirectoryForm,
                                        final BindingResult result) {
        directoryService.create(createDirectoryForm.getName(), createDirectoryForm.getParentId(), createDirectoryForm.getEmail());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/delete/{directoryId}", method = RequestMethod.DELETE)
    public ModelAndView deleteDirectory(@PathVariable("directoryId") String directoryId) {
        UUID dId = UUID.fromString(directoryId);
        directoryService.delete(dId);
        return new ModelAndView("redirect:/");
    }
}
