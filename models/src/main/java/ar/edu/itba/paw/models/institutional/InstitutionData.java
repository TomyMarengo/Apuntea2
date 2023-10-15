package ar.edu.itba.paw.models.institutional;

import java.util.*;

public class InstitutionData {
    private final Set<Institution> institutions;
    private final Map<UUID, Set<Career>> careerMap;
    private final Map<UUID, Set<Subject>> subjectMap;

    public InstitutionData() {
        institutions = new HashSet<>();
        careerMap = new HashMap<>();
        subjectMap = new HashMap<>();
    }

    public void add(Institution institution, Career career, Subject subject) {
        institutions.add(institution);
        careerMap.putIfAbsent(institution.getInstitutionId(), new HashSet<>());
        careerMap.get(institution.getInstitutionId()).add(career);
        subjectMap.putIfAbsent(career.getCareerId(), new HashSet<>());
        subjectMap.get(career.getCareerId()).add(subject);
    }
}
