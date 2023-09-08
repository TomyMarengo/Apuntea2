package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Subject;
import ar.edu.itba.apuntea.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService{
    private final SubjectDao subjectDao;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectDao.getSubjects();
    }
}
