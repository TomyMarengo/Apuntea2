package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.UUID;

public interface SubjectDao {
    List<Subject> getSubjects();
    List<Subject> getSubjectsByCareerId(UUID careerId);

    List<Subject> getSubjectsByCareerIdComplemented(UUID careerId);

    List<Subject> getSubjectsByInstitutionId(UUID institutionId);
    UUID create(String name, UUID rootDirectoryId);
    boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year);
    boolean updateSubject(UUID subjectId, String name);
    boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year);
    boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId);
}
