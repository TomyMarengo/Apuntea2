package ar.edu.itba.paw.models.institutional;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.*;

public class InstitutionData implements Serializable {

    @Expose
    private final Set<Institution> institutions;
    @Expose
    private final Map<UUID, Set<Career>> careerMap;
    @Expose
    private final Map<UUID, Set<Subject>> subjectMap;

    public InstitutionData(List<Institution> institutions) {
        this.institutions = new HashSet<>();
        this.careerMap = new HashMap<>();
        this.subjectMap = new HashMap<>();
        for (Institution i : institutions) addInstitution(i);
    }

    public void addInstitution(Institution institution) {
        for (Career c : institution.getCareers())
            for (Subject s : c.getSubjects())
                add(institution, c, s);
    }

    public void add(Institution institution, Career career, Subject subject) {
        institutions.add(institution);
        careerMap.putIfAbsent(institution.getInstitutionId(), new HashSet<>());
        careerMap.get(institution.getInstitutionId()).add(career);
        subjectMap.putIfAbsent(career.getCareerId(), new HashSet<>());
        subjectMap.get(career.getCareerId()).add(subject);
    }

    public Set<Institution> getInstitutions() {
        return institutions;
    }

    public Set<Career> getCareers(UUID institutionId) {
        return careerMap.get(institutionId);
    }

    public Set<Subject> getSubjects(UUID careerId) {
        return subjectMap.get(careerId);
    }
}
