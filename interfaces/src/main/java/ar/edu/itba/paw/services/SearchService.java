package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.search.Searchable;

import java.util.Optional;
import java.util.UUID;

public interface SearchService {
    Page<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, int page, int pageSize);
    Page<Searchable> getNavigationResults(UUID parentId, String category, String word, String sortBy, boolean ascending, int page, int pageSize);
    Optional<UUID> findByName(UUID parentId, String name);

    void delete(UUID[] noteIds, UUID[] directoryIds, String reason);
}