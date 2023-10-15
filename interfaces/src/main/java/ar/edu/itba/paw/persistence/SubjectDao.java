package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectDao {
    Optional<Subject> getSubjectById(UUID subjectId);
    List<Subject> getSubjectsByCareerId(UUID careerId);
    List<Subject> getSubjectsByCareerIdComplemented(UUID careerId);
    UUID create(String name, UUID rootDirectoryId);
    boolean delete(UUID subjectId);
    boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year);
    boolean updateSubject(UUID subjectId, String name);
    boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year);
    boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId);
}
