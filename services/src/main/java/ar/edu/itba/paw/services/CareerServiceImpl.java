package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.dtos.CareerDto;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.CareerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CareerServiceImpl implements CareerService {
    private final CareerDao careerDao;
    private final SecurityService securityService;

    @Autowired
    public CareerServiceImpl(final CareerDao careerDao, final SecurityService securityService){
        this.careerDao = careerDao;
        this.securityService = securityService;
    }

    @Transactional
    @Override
    public Optional<Career> getCareerById(UUID careerId) {
        return careerDao.getCareerById(careerId);
    }

    @Transactional
    @Override
    public List<CareerDto> getCareersByCurrentUserInstitution() {
        User currentUser = securityService.getCurrentUserOrThrow();
        return careerDao.getCareersByUserInstitution(currentUser)
                .stream()
                .map(CareerDto::new)
                .collect(Collectors.toList());
    }

}
