package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.search.SortArguments;
import ar.edu.itba.paw.models.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryDao {
    Directory create(String name, UUID parentId, User user, boolean visible, String iconColor);

    Directory createRootDirectory(String name);

    Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId);

    List<UUID> getDirectoryPathIds(UUID directoryId);

    boolean delete(List<UUID> directoryIds, UUID currentUserId);

    boolean delete(List<UUID> directoryId); // Strong delete

    List<Directory> getFavorites(UUID userId);

    void addFavorite(UUID userId, UUID directoryId);

    boolean removeFavorite(UUID userId, UUID directoryId);

    List<Directory> findDirectoriesByIds(List<UUID> directoryIds, User currentUser, SortArguments sortArgs);

    List<Directory> findDirectoriesByIds(List<UUID> directoryIds);
}

