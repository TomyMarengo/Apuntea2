package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryDao {
    UUID create(String name, UUID parentId, UUID userId, boolean visible, String iconColor);

    UUID createRootDirectory(String name);

    Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId);
    DirectoryPath getDirectoryPath(UUID directoryId);

    boolean update(Directory directory, UUID currentUserId);

    boolean delete(UUID[] directoryIds, UUID currentUserId);

    List<Directory> delete(UUID[] directoryId); // Strong delete

    boolean deleteRootDirectory(UUID directoryId);

    List<Directory> getFavorites(UUID userId);

    void addFavorite(UUID userId, UUID directoryId);

    boolean removeFavorite(UUID userId, UUID directoryId);
}
