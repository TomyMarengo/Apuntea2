package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.directory.DirectoryPath;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> getDirectoryById(UUID directoryId);
    UUID create(String name, UUID parentId, boolean visible, String iconColor);
    DirectoryPath getDirectoryPath(UUID directoryId);
    void update(UUID directoryId, String name, boolean visible, String iconColor);
    void delete(UUID[] directoryIds, String reason);
    DirectoryFavoriteGroups getFavorites();
    void addFavorite(UUID directoryId);
    void removeFavorite(UUID directoryId);
}

