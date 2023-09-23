package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.Searchable;

import java.util.List;
import java.util.UUID;

public interface SearchService {
    List<Searchable> search(UUID institutionId, UUID careerId, UUID subjectId, String category, String word, String sortBy, boolean ascending, Integer page, Integer pageSize);
}