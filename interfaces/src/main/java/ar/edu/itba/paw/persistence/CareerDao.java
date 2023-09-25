package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Career;
import ar.edu.itba.paw.models.Institution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerDao {
    List<Career> getCareers();

    Optional<Career> findCareerById(UUID careerId);

}
