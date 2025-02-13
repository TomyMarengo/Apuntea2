package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.institutional.SubjectCareerNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.SubjectNotFoundException;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;
    private final DirectoryDao directoryDao;
    private final UserDao userDao;
    private final CareerDao careerDao;
    private final SearchDao searchDao;
    private final SecurityService securityService;



    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, DirectoryDao directoryDao, UserDao userDao, CareerDao careerDao, SearchDao searchDao, SecurityService securityService) {
        this.subjectDao = subjectDao;
        this.directoryDao = directoryDao;
        this.userDao = userDao;
        this.careerDao = careerDao;
        this.searchDao = searchDao;
        this.securityService = securityService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subject> getSubject(UUID subjectId) {
        return subjectDao.getSubjectById(subjectId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjects(UUID careerId, Integer year, UUID userId) {
        User user = null;
        if (userId != null) {
            user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        }
        return subjectDao.getSubjects(careerId, year, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsByCareerComplemented(UUID careerId) {
        return subjectDao.getSubjectsByCareerIdComplemented(careerId);
    }

    @Override
    @Transactional
    public UUID createSubject(String name) {
        Directory rootDirectory = directoryDao.createRootDirectory(name);
        Subject subject = subjectDao.create(name, rootDirectory.getId());
        return subject.getSubjectId();
    }

    @Override
    @Transactional
    public void updateSubject(UUID subjectId, String name) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(SubjectNotFoundException::new);
        subject.setName(name);
    }


    @Override
    @Transactional
    public void deleteSubject(UUID subjectId) {
        Optional<Subject> maybeSubject = subjectDao.getSubjectById(subjectId);
        if (!maybeSubject.isPresent()) return;
        UUID rootDirectoryId = maybeSubject.get().getRootDirectoryId();
        subjectDao.delete(maybeSubject.get());
        directoryDao.delete(rootDirectoryId);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectCareer> getSubjectCareer(UUID subjectId, UUID careerId) {
        return subjectDao.getSubjectCareer(subjectId, careerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectCareer> getSubjectCareers(UUID careerId) {
        return subjectDao.getSubjectCareers(careerId);
    }

    @Override
    @Transactional
    public boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        return subjectDao.linkSubjectToCareer(subject, careerId, year);
    }
    @Override
    @Transactional
    public void updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
        boolean success = subjectDao.updateSubjectCareer(subjectId, careerId, year);
        if (!success)
            throw new SubjectCareerNotFoundException();
    }


    @Override
    @Transactional
    public boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        return subjectDao.unlinkSubjectFromCareer(subjectId, careerId);
    }

    @Transactional
    @Override
    public boolean isSubjectDetachable(UUID subjectId) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        return careerDao.countCareersBySubjectId(subjectId) > 1 || searchDao.countChildren(subject.getRootDirectoryId()) == 0;
    }

}
