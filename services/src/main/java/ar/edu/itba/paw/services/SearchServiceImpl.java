package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.persistence.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SearchServiceImpl implements SearchService {
    private final SecurityService securityService;
    private final SearchDao searchDao;

    @Autowired
    public SearchServiceImpl(final SearchDao searchDao, final SecurityService securityService, final EmailService emailService) {
        this.searchDao = searchDao;
        this.securityService = securityService;
    }

    @Override
    public List<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        SearchArguments sa = new SearchArguments(institutionId, careerId, subjectId, category, word, sortBy, ascending, page, pageSize);
        return searchDao.search(sa);
    }

}
