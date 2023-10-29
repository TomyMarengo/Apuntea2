package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectCareerException;
import ar.edu.itba.paw.persistence.CareerDao;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.SearchDao;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;
    private final DirectoryDao directoryDao;
    private final CareerDao careerDao;
    private final SearchDao searchDao;
    private final SecurityService securityService;


    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, DirectoryDao directoryDao, CareerDao careerDao, SearchDao searchDao, SecurityService securityService) {
        this.subjectDao = subjectDao;
        this.directoryDao = directoryDao;
        this.careerDao = careerDao;
        this.searchDao = searchDao;
        this.securityService = securityService;
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
    public UUID createSubject(String name, UUID careerId, int year) {
        Directory rootDirectory = directoryDao.createRootDirectory(name);
        UUID subjectId = subjectDao.create(name, rootDirectory.getId());
        subjectDao.linkSubjectToCareer(subjectId, careerId, year);
        return subjectId;
    }

    @Override
    @Transactional
    public void linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        boolean success = subjectDao.linkSubjectToCareer(subjectId, careerId, year);
        if (!success) {
            throw new InvalidSubjectCareerException();
        }
    }

    @Override
    @Transactional
    public void updateSubject(UUID subjectId, String name) {
        boolean success = subjectDao.updateSubject(subjectId, name);
        if (!success) {
            throw new InvalidSubjectException();
        }
    }

    @Override
    @Transactional
    public void updateSubjectCareer(UUID subjectId, String subjectName, UUID careerId, int year) {
        updateSubject(subjectId, subjectName);
        boolean success = subjectDao.updateSubjectCareer(subjectId, careerId, year);
        if (!success) {
            throw new InvalidSubjectCareerException();
        }
    }

    @Override
    @Transactional
    public void unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        boolean success = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);
        if (!success) {
            throw new InvalidSubjectCareerException();
        } else if (careerDao.countCareersBySubjectId(subjectId) == 0 && searchDao.countChildren(subject.getRootDirectoryId()) == 0) {
            success = subjectDao.delete(subjectId);
            if (!success)
                throw new InvalidSubjectException();
            success = directoryDao.delete(Collections.singletonList(subject.getRootDirectoryId()));
            if (!success)
                throw new InvalidDirectoryException();
        }
    }

    @Transactional
    @Override
    public boolean isSubjectDetachable(UUID subjectId) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        return careerDao.countCareersBySubjectId(subjectId) > 1 || searchDao.countChildren(subject.getRootDirectoryId()) == 0;
    }

}
