package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerDao {
    Optional<Career> getCareerById(UUID careerId);
    Collection<Career> getCareers(UUID institutionId);

    int countCareersBySubjectId(UUID subjectId);

    List<Career> getCareersByUserInstitution(User user);
}
