package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.SearchArguments;

import java.util.List;
import java.util.UUID;

public interface DirectoryDao {
    Directory create(String name, UUID parentId, UUID userId);

    List<Directory> search(SearchArguments sa);

    Directory getDirectoryById(UUID directoryId);

    List<Directory> getChildren(UUID directoryId);

    DirectoryPath getDirectoryPath(UUID directoryId);

    void delete(UUID directoryId);
}
