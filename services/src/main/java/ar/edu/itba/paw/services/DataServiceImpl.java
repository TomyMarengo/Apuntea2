package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
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
    public List<Note> searchNotes(UUID institutionId, UUID careerId, UUID subjectId, String category, Float score, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        return noteService.searchNotes(institutionId, careerId, subjectId, category, score, word, sortBy, ascending, page, pageSize);
    }

    @Override
    public Note createNote(MultipartFile file, String name, String email, UUID subjectId, String category) {
        return noteService.createNote(file, name, email, subjectId, category);
    }

    @Override
    public Optional<Note> getNoteById(UUID noteId) {
        return noteService.getNoteById(noteId);
    }
    @Override
    public byte[] getNoteFileById(UUID noteId) {
        return noteService.getNoteFileById(noteId);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, UUID userId, Integer score) {
        return noteService.createOrUpdateReview(noteId, userId, score);
    }

    @Override
    public Integer createOrUpdateReview(UUID noteId, String email, Integer score) {
        return noteService.createOrUpdateReview(noteId, email, score);
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
