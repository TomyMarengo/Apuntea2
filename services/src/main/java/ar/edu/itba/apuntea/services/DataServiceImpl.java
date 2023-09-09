package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Career;
import ar.edu.itba.apuntea.models.Institution;
import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
public class DataServiceImpl implements DataService {
    // Fachade pattren to access all services
    private final NoteService noteService;
    private final InstitutionService institutionService;
    private final CareerService careerService;
    private final SubjectService subjectService;

    @Autowired
    public DataServiceImpl(final NoteService noteService, final InstitutionService institutionService, final CareerService careerService, final SubjectService subjectService) {
        this.noteService = noteService;
        this.institutionService = institutionService;
        this.careerService = careerService;
        this.subjectService = subjectService;
    }

    @Override
    public List<Note> searchNotes(String institution, String career, String subject, String category, Float score, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        return noteService.searchNotes(institution, career, subject, category, score, sortBy, ascending, page, pageSize);
    }

    @Override
    public Note createNote(MultipartFile file, String name) {
        return noteService.createNote(file, name);
    }

    @Override
    public byte[] getNoteFileById(UUID noteId) {
        return noteService.getNoteFileById(noteId);
    }

    @Override
    public List<Institution> getInstitutions() {
        return institutionService.getInstitutions();
    }

    @Override
    public List<Career> getCareers() {
        return careerService.getCareers();
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }

}
