package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectDao {
    Optional<Subject> getSubjectById(UUID subjectId);
    UUID getSubjectByRootDirectoryId(UUID rootDirectoryId);
    List<Subject> getSubjectsByCareerId(UUID careerId);
    List<Subject> getSubjectsByCareerIdComplemented(UUID careerId);
    List<Subject> getSubjectsByUser(User user);
    Subject create(String name, UUID rootDirectoryId);
    boolean delete(UUID subjectId);
    boolean linkSubjectToCareer(Subject subject, UUID careerId, int year);
    boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year);
    boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId);
}
