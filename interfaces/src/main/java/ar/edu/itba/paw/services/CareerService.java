package ar.edu.itba.paw.services;import ar.edu.itba.paw.models.institutional.Career;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerService {
    Collection<Career> getCareers(UUID institutionId);
    Optional<Career> getCareerById(UUID careerId);

}
