package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.UUID;

public interface SubjectService {
    List<Subject> getSubjects();
    List<Subject> getSubjectsByCareer(UUID careerId);
    List<Subject> getSubjectsByCareerComplemented(UUID careerId);
    List<Subject> getSubjectsByInstitution(UUID institutionId);

    UUID createSubject(String name, UUID careerId, int year);
    void linkSubjectToCareer(UUID subjectId, UUID careerId, int year);

    void updateSubject(UUID subjectId, String name);
    void updateSubjectCareer(UUID subjectId, String subjectName, UUID careerId, int year);

    void unlinkSubjectFromCareer(UUID subjectId, UUID careerId);

}
