package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Directory;
import ar.edu.itba.apuntea.models.Note;
import ar.edu.itba.apuntea.models.SearchArguments;

import java.util.List;
import java.util.UUID;

public interface DirectoryDao {
    Directory create(String name, UUID parentId, UUID userId);
    List<Directory> search(SearchArguments sa);
}
