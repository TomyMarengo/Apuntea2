package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryDao {
    UUID create(String name, UUID parentId, UUID userId, boolean visible, String iconColor);

    UUID createRootDirectory(String name);
    DirectoryPath getDirectoryPath(UUID directoryId);

    boolean update(Directory directory, UUID currentUserId);
    Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId);

    boolean delete(UUID[] directoryIds, UUID currentUserId);
    List<Directory> delete(UUID[] directoryId); // Strong delete
    List<Directory> getRootDirectoriesByCareer(UUID careerId);

    List<Directory> getFavorites(UUID userId);

    void addFavorite(UUID userId, UUID directoryId);

    boolean removeFavorite(UUID userId, UUID directoryId);
}
