package ar.edu.itba.paw.models.institutional;

import ar.edu.itba.paw.models.institutional.dtos.CareerDto;
import ar.edu.itba.paw.models.institutional.dtos.InstitutionDto;
import ar.edu.itba.paw.models.institutional.dtos.SubjectDto;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.*;

public class InstitutionDataDto implements Serializable {

    private final Set<InstitutionDto> institutions;

    private final Map<UUID, Set<CareerDto>> careerMap;

    private final Map<UUID, Set<SubjectDto>> subjectMap;

    public InstitutionDataDto() {
        this.institutions = new HashSet<>();
        this.careerMap = new HashMap<>();
        this.subjectMap = new HashMap<>();
    }

    public void addInstitution(Institution institution) {
        for (Career c : institution.getCareers())
            for (Subject s : c.getSubjects())
                add(institution, c, s);
    }

    public void add(Institution institution, Career career, Subject subject) {
        institutions.add(new InstitutionDto(institution));
        careerMap.putIfAbsent(institution.getInstitutionId(), new HashSet<>());
        careerMap.get(institution.getInstitutionId()).add(new CareerDto(career));
        subjectMap.putIfAbsent(career.getCareerId(), new HashSet<>());
        subjectMap.get(career.getCareerId()).add(new SubjectDto(subject));
    }

    public Set<InstitutionDto> getInstitutions() {
        return institutions;
    }

    public Set<CareerDto> getCareers(UUID institutionId) {
        return careerMap.get(institutionId);
    }

    public Set<SubjectDto> getSubjects(UUID careerId) {
        return subjectMap.get(careerId);
    }



}
