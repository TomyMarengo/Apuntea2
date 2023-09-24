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
    public SearchServiceImpl(final SearchDao searchDao, final SecurityService securityService) {
        this.searchDao = searchDao;
        this.securityService = securityService;
    }

    @Override
    public List<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
        SearchArguments sa = new SearchArguments()
                .withInstitutionId(institutionId)
                .withCareerId(careerId)
                .withSubjectId(subjectId)
                .withCategory(category)
                .withWord(word)
                .withSortBy(sortBy)
                .withAscending(ascending)
                .withPage(page)
                .withPageSize(pageSize);
        securityService.getCurrentUser().ifPresent(u -> sa.withCurrentUserId(u.getUserId()));
        return searchDao.search(sa);
    }

    @Override
    public List<Searchable> getNavigationResults(UUID parentId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
        SearchArguments sa = new SearchArguments().withCategory(category).withWord(word).withSortBy(sortBy).withAscending(ascending).withPage(page).withPageSize(pageSize);
        securityService.getCurrentUser().ifPresent(u -> sa.withCurrentUserId(u.getUserId()));
        return searchDao.getNavigationResults(sa, parentId);
    }

}
