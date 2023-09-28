package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;

import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> getDirectoryById(UUID directoryId);
    UUID create(String name, UUID parentId, boolean visible, String iconColor);
    DirectoryPath getDirectoryPath(UUID directoryId);

    void update(Directory directory);
    void delete(UUID directoryId);
}
