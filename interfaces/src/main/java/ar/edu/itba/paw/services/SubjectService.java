package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
//import ar.edu.itba.paw.models.institutional.dtos.SubjectDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SubjectService {
    Optional<SubjectCareer> getSubjectCareer(UUID subjectId, UUID careerId);
    List<Subject> getSubjectsByCareerComplemented(UUID careerId);
    List<Subject> getSubjectsByCareer(UUID careerId, Integer year);
    Map<Integer, List<Subject>> getSubjectsByUserIdGroupByYear(UUID userId);
    UUID createSubject(String name);
    boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year);

    void updateSubject(UUID subjectId, String name);
    void updateSubjectCareer(UUID subjectId, UUID careerId, int year);
    void deleteSubject(UUID subjectId);
    boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId);

    boolean isSubjectDetachable(UUID subjectId);

}
