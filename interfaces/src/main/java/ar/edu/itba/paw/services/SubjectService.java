package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectService {
    Optional<Subject> getSubject(UUID subjectId);
    Optional<SubjectCareer> getSubjectCareer(UUID subjectId, UUID careerId);
    List<Subject> getSubjects(UUID careerId, Integer year, UUID userId);
    List<SubjectCareer> getSubjectCareers(UUID careerId);
    List<Subject> getSubjectsByCareerComplemented(UUID careerId);
    UUID createSubject(String name);
    boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year);

    void updateSubject(UUID subjectId, String name);
    void updateSubjectCareer(UUID subjectId, UUID careerId, int year);
    void deleteSubject(UUID subjectId);
    boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId);

    boolean isSubjectDetachable(UUID subjectId);

}
