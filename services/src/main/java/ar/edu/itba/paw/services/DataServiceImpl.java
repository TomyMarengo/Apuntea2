package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.Institution;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public List<Career> getCareers() {
        return careerService.getCareers();
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }
}
