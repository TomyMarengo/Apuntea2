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

//    @Transactional
//    @Override
//    public Page<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, UUID userId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
//                .institutionId(institutionId)
//                .careerId(careerId)
//                .subjectId(subjectId)
//                .userId(userId)
//                .category(category)
//                .word(word)
//                .sortBy(sortBy)
//                .ascending(ascending);
//
//        Optional<User> maybeUser = securityService.getCurrentUser();
//        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));
//
//        SearchArguments searchArgumentsWithoutPaging = sab.build();
//        int countTotalResults = searchDao.countSearchResults(searchArgumentsWithoutPaging);
//        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);
//
//        sab.page(safePage).pageSize(pageSize);
//        SearchArguments sa = sab.build();
//
//        return new Page<>(
//                searchDao.search(sa),
//                sa.getPage(),
//                sa.getPageSize(),
//                countTotalResults
//        );
//    }
//
//    @Transactional
//    @Override
//    public Page<Searchable> getNavigationResults(UUID parentId, UUID userId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
//                .userId(userId)
//                .category(category)
//                .userId(userId)
//                .word(word)
//                .sortBy(sortBy)
//                .ascending(ascending);
//        Optional<User> maybeUser = securityService.getCurrentUser();
//        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));
//
//        SearchArguments searchArgumentsWithoutPaging = sab.build();
//        int countTotalResults = searchDao.countNavigationResults(searchArgumentsWithoutPaging, parentId);
//        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);
//
//        sab.page(safePage).pageSize(pageSize);
//        SearchArguments sa = sab.build();
//
//        return new Page<>(
//                searchDao.getNavigationResults(sa, parentId),
//                sa.getPage(),
//                sa.getPageSize(),
//                countTotalResults
//        );
//    }

    @Transactional
    @Override
    public Optional<UUID> findByName(UUID parentId, String name) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        return searchDao.findByName(parentId, name, currentUserId);
    }

}
