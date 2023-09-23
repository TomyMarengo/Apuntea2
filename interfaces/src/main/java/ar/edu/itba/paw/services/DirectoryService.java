package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> getDirectoryById(UUID directoryId);
    UUID create(String name, UUID parentId);
    DirectoryPath getDirectoryPath(UUID directoryId);
    void delete(UUID directoryId);
}
