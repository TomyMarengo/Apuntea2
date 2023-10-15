package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> getDirectoryById(UUID directoryId);
    UUID create(String name, UUID parentId, boolean visible, String iconColor);
    DirectoryPath getDirectoryPath(UUID directoryId);
    void update(Directory directory);
    void delete(UUID[] directoryIds, String reason);
    List<Directory> getFavorites();
    void addFavorite(UUID directoryId);
    boolean removeFavorite(UUID directoryId);
}

