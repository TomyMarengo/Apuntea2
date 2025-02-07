package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.search.Searchable;

import java.util.Optional;
import java.util.UUID;

public interface SearchService {
    Optional<UUID> findByName(UUID parentId, String name);

}