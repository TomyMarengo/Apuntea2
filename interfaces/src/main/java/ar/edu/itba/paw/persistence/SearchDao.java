package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Pair;
import ar.edu.itba.paw.models.search.SearchArguments;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchDao {
    List<Pair<UUID, Boolean>> search(SearchArguments sa);
    List<Pair<UUID, Boolean>> getNavigationResults(SearchArguments sa, UUID parentId);
    int countSearchResults(SearchArguments sa);
    int countNavigationResults(SearchArguments sa, UUID parentId);
    Optional<UUID> findByName(UUID parentId, String name, UUID currentUserId);
    int countChildren(UUID parentId);
}
