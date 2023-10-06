package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.InvalidSubjectException;
import ar.edu.itba.paw.models.exceptions.InvalidSubjecteCareerException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubjectServiceImpl implements SubjectService{
    private final SubjectDao subjectDao;
    private final DirectoryDao directoryDao;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, DirectoryDao directoryDao) {
        this.subjectDao = subjectDao;
        this.directoryDao = directoryDao;
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
    public List<Subject> getSubjectsByInstitution(UUID institutionId) {
        return subjectDao.getSubjectsByInstitutionId(institutionId);
    }

    @Override
    @Transactional
    public UUID createSubject(String name, UUID careerId, int year) {
        UUID rootDirectoryId = directoryDao.createRootDirectory(name);
        UUID subjectId = subjectDao.create(name, rootDirectoryId);
        subjectDao.addSubjectToCareer(subjectId, careerId, year);
        return subjectId;
    }

    @Override
    @Transactional
    public void addSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        boolean result = subjectDao.addSubjectToCareer(subjectId, careerId, year);
        if (!result) {
            throw new InvalidSubjecteCareerException();
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
    public void updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
        boolean result = subjectDao.updateSubjectCareer(subjectId, careerId, year);
        if (!result) {
            throw new InvalidSubjecteCareerException();
        }
    }

    @Override
    @Transactional
    public void removeSubjectFromCareer(UUID subjectId, UUID careerId) {
        boolean result = subjectDao.removeSubjectFromCareer(subjectId, careerId);
        if (!result) {
            throw new InvalidSubjecteCareerException();
        }
    }
}
