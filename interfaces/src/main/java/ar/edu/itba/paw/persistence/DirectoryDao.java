package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.SearchArguments;

import java.util.List;
import java.util.UUID;

public interface DirectoryDao {
    UUID create(String name, UUID parentId, UUID userId);
    Directory getDirectoryById(UUID directoryId);
    DirectoryPath getDirectoryPath(UUID directoryId);
    void delete(UUID directoryId);
}
