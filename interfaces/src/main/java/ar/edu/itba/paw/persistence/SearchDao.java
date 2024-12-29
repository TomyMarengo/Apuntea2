package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.search.Searchable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchDao {
    Optional<UUID> findByName(UUID parentId, String name, UUID currentUserId);
    int countChildren(UUID parentId);
}
