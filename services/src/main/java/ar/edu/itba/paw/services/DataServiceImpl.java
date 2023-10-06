package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.models.InstitutionData;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DataServiceImpl implements DataService {
    private final InstitutionService institutionService;
    private final CareerService careerService;
    private final SubjectService subjectService;

    @Autowired
    public DataServiceImpl(final InstitutionService institutionService, final CareerService careerService, final SubjectService subjectService) {
        this.institutionService = institutionService;
        this.careerService = careerService;
        this.subjectService = subjectService;
    }

    @Override
    public List<Institution> getInstitutions() {
        return institutionService.getInstitutions();
    }

    @Override
    public Optional<Institution> findInstitutionById(UUID institutionId) {
        return institutionService.findInstitutionById(institutionId);
    }

    @Override
    public Optional<Career> findCareerById(UUID careerId) {
        return careerService.findCareerById(careerId);
    }

    @Override
    public List<Career> getCareers() {
        return careerService.getCareers();
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }

    @Override
    public List<Subject> getSubjectsByCareer(UUID careerId) {
        return subjectService.getSubjectsByCareer(careerId);
    }

    @Override
    public List<Subject> getSubjectsByInstitution(UUID institutionId) {
        return subjectService.getSubjectsByInstitution(institutionId);
    }

    @Override
    public InstitutionData getInstitutionData() {
        return institutionService.getInstitutionData();
    }

    @Override
    @Transactional
    public UUID createSubject(String name, UUID careerId, int year) {
        return subjectService.createSubject(name, careerId, year);
    }

    @Override
    @Transactional
    public void addSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        subjectService.addSubjectToCareer(subjectId, careerId, year);
    }

    @Override
    @Transactional
    public void updateSubject(UUID subjectId, String name) {
        subjectService.updateSubject(subjectId, name);
    }

    @Override
    public void updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
        subjectService.updateSubjectCareer(subjectId, careerId, year);
    }

    @Override
    @Transactional
    public void removeSubjectFromCareer(UUID subjectId, UUID careerId) {
        subjectService.removeSubjectFromCareer(subjectId, careerId);
    }
}
