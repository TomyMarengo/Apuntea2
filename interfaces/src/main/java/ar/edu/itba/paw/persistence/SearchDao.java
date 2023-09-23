package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.Searchable;

import java.util.List;

public interface SearchDao {
    List<Searchable> search(SearchArguments sa);
}
