package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.dtos.SubjectDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SubjectService {
    List<Subject> getSubjectsByCareer(UUID careerId);
    List<SubjectDto> getSubjectsByCareerComplemented(UUID careerId);
    Map<Integer, List<Subject>> getSubjectsByCareerGroupByYear();
    Map<Integer, List<Subject>> getSubjectsByUserIdGroupByYear(UUID userId);
    UUID createSubject(String name, UUID careerId, int year);
    void linkSubjectToCareer(UUID subjectId, UUID careerId, int year);

    void updateSubject(UUID subjectId, String name);
    void updateSubjectCareer(UUID subjectId, String subjectName, UUID careerId, int year);

    void unlinkSubjectFromCareer(UUID subjectId, UUID careerId);

    boolean isSubjectDetachable(UUID subjectId);

}
