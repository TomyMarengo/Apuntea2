package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerDao {

    Optional<Career> getCareerById(UUID careerId);

    int countCareersBySubjectId(UUID subjectId);

}
