package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Subject;

import java.util.List;
import java.util.UUID;

public interface SubjectDao {
    List<Subject> getSubjects();
    List<Subject> getSubjectsByCareerId(UUID careerId);
    List<Subject> getSubjectsByInstitutionId(UUID institutionId);
}
