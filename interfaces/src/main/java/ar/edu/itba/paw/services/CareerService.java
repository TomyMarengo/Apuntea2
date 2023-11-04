package ar.edu.itba.paw.services;import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.dtos.CareerDto;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerService {
    Optional<Career> getCareerById(UUID careerId);
    List<CareerDto> getCareersByCurrentUserInstitution();
}
