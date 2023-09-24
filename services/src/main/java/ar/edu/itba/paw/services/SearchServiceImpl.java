package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.Searchable;
import ar.edu.itba.paw.persistence.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.SearchArguments.SearchArgumentsBuilder;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Page<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
                .institutionId(institutionId)
                .careerId(careerId)
                .subjectId(subjectId)
                .category(category)
                .word(word)
                .sortBy(sortBy)
                .ascending(ascending)
                .page(page)
                .pageSize(pageSize);
        securityService.getCurrentUser().ifPresent(u -> sab.currentUserId(u.getUserId()));
        SearchArguments sa = sab.build();

        return new Page<>(
                searchDao.search(sa),
                sa.getPage(),
                sa.getPageSize(),
                searchDao.countSearchResults(sa)
        );
    }

    @Transactional
    @Override
    public Page<Searchable> getNavigationResults(UUID parentId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
                .category(category)
                .word(word)
                .sortBy(sortBy)
                .ascending(ascending)
                .page(page)
                .pageSize(pageSize);
        securityService.getCurrentUser().ifPresent(u -> sab.currentUserId(u.getUserId()));
        SearchArguments sa = sab.build();
        return new Page<>(
                searchDao.getNavigationResults(sa, parentId),
                sa.getPage(),
                sa.getPageSize(),
                searchDao.countNavigationResults(sa, parentId)
        );
    }

}
