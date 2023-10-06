package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.UUID;

public interface SubjectService {
    List<Subject> getSubjects();
    List<Subject> getSubjectsByCareer(UUID careerId);
    List<Subject> getSubjectsByInstitution(UUID institutionId);

    UUID createSubject(String name, UUID careerId, int year);
    void addSubjectToCareer(UUID subjectId, UUID careerId, int year);

    void updateSubject(UUID subjectId, String name);
    void updateSubjectCareer(UUID subjectId, UUID careerId, int year);

    void removeSubjectFromCareer(UUID subjectId, UUID careerId);

}
