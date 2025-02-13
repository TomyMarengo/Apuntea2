package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.search.SearchArguments;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> getDirectoryById(UUID directoryId);
    UUID create(String name, UUID parentId, boolean visible, String iconColor);
    DirectoryPath getDirectoryPath(UUID directoryId);
    void update(UUID directoryId, String name, Boolean visible, String iconColor);
    void delete(UUID directoryId, String reason);
    void delete(UUID directoryId);

    boolean isFavorite(UUID directoryId);
    boolean addFavorite(UUID directoryId);
    boolean removeFavorite(UUID directoryId);
    Page<Directory> getDirectories(UUID parentId, UUID userId, UUID favBy, String word, UUID institutionId, UUID careerId, UUID subjectId, Boolean isRdir, String sortBy, boolean ascending, int page, int pageSize);
}

