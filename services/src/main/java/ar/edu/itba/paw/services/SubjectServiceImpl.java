package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.InvalidSubjectException;
import ar.edu.itba.paw.models.exceptions.InvalidSubjectCareerException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;
    private final DirectoryDao directoryDao;
    private final SecurityService securityService;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, DirectoryDao directoryDao, SecurityService securityService) {
        this.subjectDao = subjectDao;
        this.directoryDao = directoryDao;
        this.securityService = securityService;
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectDao.getSubjects();
    }

    @Override
    @Transactional
    public List<Subject> getSubjectsByCareer(UUID careerId) {
        return subjectDao.getSubjectsByCareerId(careerId);
    }

    @Override
    @Transactional
    public Map<Integer, List<Subject>> getSubjectsByCareerGroupByYear(UUID careerId) {
        List<Subject> subjects = subjectDao.getSubjectsByCareerId(careerId);
        return subjects.stream().collect(Collectors.groupingBy(Subject::getYear));
    }

    @Override
    @Transactional
    public Map<Integer, List<Subject>> getSubjectsByCareerGroupByYear() {
        User user = this.securityService.getCurrentUserOrThrow();
        return getSubjectsByCareerGroupByYear(user.getCareer().getCareerId());
    }

    @Override
    @Transactional
    public List<Subject> getSubjectsByCareerComplemented(UUID careerId) {
        return subjectDao.getSubjectsByCareerIdComplemented(careerId);
    }

    @Override
    @Transactional
    public List<Subject> getSubjectsByInstitution(UUID institutionId) {
        return subjectDao.getSubjectsByInstitutionId(institutionId);
    }

    @Override
    @Transactional
    public UUID createSubject(String name, UUID careerId, int year) {
        UUID rootDirectoryId = directoryDao.createRootDirectory(name);
        UUID subjectId = subjectDao.create(name, rootDirectoryId);
        subjectDao.linkSubjectToCareer(subjectId, careerId, year);
        return subjectId;
    }

    @Override
    @Transactional
    public void linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        boolean result = subjectDao.linkSubjectToCareer(subjectId, careerId, year);
        if (!result) {
            throw new InvalidSubjectCareerException();
        }
    }

    @Override
    @Transactional
    public void updateSubject(UUID subjectId, String name) {
        boolean result = subjectDao.updateSubject(subjectId, name);
        if (!result) {
            throw new InvalidSubjectException();
        }
    }

    @Override
    @Transactional
    public void updateSubjectCareer(UUID subjectId, String subjectName, UUID careerId, int year) {
        updateSubject(subjectId, subjectName);
        boolean result = subjectDao.updateSubjectCareer(subjectId, careerId, year);
        if (!result) {
            throw new InvalidSubjectCareerException();
        }
    }

    @Override
    @Transactional
    public void unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        boolean result = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);
        if (!result) {
            throw new InvalidSubjectCareerException();
        }
    }
}
