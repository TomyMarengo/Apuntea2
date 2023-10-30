package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.NoteDao;
import ar.edu.itba.paw.persistence.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.SearchArguments.SearchArgumentsBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SearchServiceImpl implements SearchService {
    private final SecurityService securityService;
    private final SearchDao searchDao;

    private final NoteService noteService;

    private final DirectoryService directoryService;

    private final NoteDao noteDao;

    private final DirectoryDao directoryDao;

    @Autowired
    public SearchServiceImpl(final SearchDao searchDao,
                             final SecurityService securityService,
                             final NoteService noteService,
                             final DirectoryService directoryService,
                             final NoteDao noteDao,
                             final DirectoryDao directoryDao
    ) {
        this.searchDao = searchDao;
        this.securityService = securityService;
        this.noteService = noteService;
        this.directoryService = directoryService;
        this.noteDao = noteDao;
        this.directoryDao = directoryDao;
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
                .ascending(ascending);

        Optional<User> maybeUser = securityService.getCurrentUser();
        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));

        SearchArguments searchArgumentsWithoutPaging = sab.build();
        int countTotalResults = searchDao.countSearchResults(searchArgumentsWithoutPaging);
        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);

        sab.page(safePage).pageSize(pageSize);
        SearchArguments sa = sab.build();
        List<Pair<UUID, Boolean>> ids = searchDao.search(sa);
        //TODO: optimize?
        //TODO: Modularize
        List<UUID> noteIds = ids.stream().filter(Pair::getValue).map(Pair::getKey).collect(java.util.stream.Collectors.toList());
        List<UUID> directoryIds = ids.stream().filter(p -> !p.getValue()).map(Pair::getKey).collect(java.util.stream.Collectors.toList());

        List<Searchable> results = new ArrayList<>();

        if (!directoryIds.isEmpty())
            results.addAll(directoryDao.findDirectoriesByIds(directoryIds, maybeUser.orElse(null), sa.getSortBy(), sa.isAscending()));
        if (!noteIds.isEmpty())
            results.addAll(noteDao.findNoteByIds(noteIds, sa.getSortBy(), sa.isAscending()));

        return new Page<>(
                results,
                sa.getPage(),
                sa.getPageSize(),
                countTotalResults
        );
    }

    @Transactional
    @Override
    public Page<Searchable> getNavigationResults(UUID parentId, String category, String word, String sortBy, boolean ascending, int page, int pageSize) {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
                .category(category)
                .word(word)
                .sortBy(sortBy)
                .ascending(ascending);
        Optional<User> maybeUser = securityService.getCurrentUser();
        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));

        SearchArguments searchArgumentsWithoutPaging = sab.build();
        int countTotalResults = searchDao.countNavigationResults(searchArgumentsWithoutPaging, parentId);
        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);

        sab.page(safePage).pageSize(pageSize);
        SearchArguments sa = sab.build();

        List<Pair<UUID, Boolean>> ids = searchDao.getNavigationResults(sa, parentId);
        List<UUID> noteIds = ids.stream().filter(Pair::getValue).map(Pair::getKey).collect(java.util.stream.Collectors.toList());
        List<UUID> directoryIds = ids.stream().filter(p -> !p.getValue()).map(Pair::getKey).collect(java.util.stream.Collectors.toList());

        List<Searchable> results = new ArrayList<>();

        if (!directoryIds.isEmpty())
            results.addAll(directoryDao.findDirectoriesByIds(directoryIds, maybeUser.orElse(null), sa.getSortBy(), sa.isAscending()));
        if (!noteIds.isEmpty())
            results.addAll(noteDao.findNoteByIds(noteIds, sa.getSortBy(), sa.isAscending()));

        return new Page<>(
                results,
                sa.getPage(),
                sa.getPageSize(),
                countTotalResults
        );
    }

    @Transactional
    @Override
    public Optional<UUID> findByName(UUID parentId, String name) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        return searchDao.findByName(parentId, name, currentUserId);
    }

    @Transactional
    @Override
    public void delete(UUID[] noteIds, UUID[] directoryIds, String reason) {
        if(noteIds.length > 0)
            noteService.delete(noteIds, reason);
        if(directoryIds.length > 0)
            directoryService.delete(directoryIds, reason);
    }
}
