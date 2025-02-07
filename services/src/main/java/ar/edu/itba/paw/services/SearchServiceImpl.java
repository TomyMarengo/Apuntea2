package ar.edu.itba.paw.services;


import ar.edu.itba.paw.persistence.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SearchServiceImpl implements SearchService {
    private final SecurityService securityService;
    private final SearchDao searchDao;


    @Autowired
    public SearchServiceImpl(final SearchDao searchDao,
                             final SecurityService securityService
    ) {
        this.searchDao = searchDao;
        this.securityService = securityService;
    }

    @Transactional
    @Override
    public Optional<UUID> findByName(UUID parentId, String name) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        return searchDao.findByName(parentId, name, currentUserId);
    }

}
