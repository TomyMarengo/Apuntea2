package ar.edu.itba.paw.persistence;

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

    List<Directory> getDirectoryPath(UUID directoryId);

    Optional<Directory> getDirectoryRoot(UUID directoryId);

    boolean delete(List<UUID> directoryIds, UUID currentUserId);

    boolean delete(List<UUID> directoryId); // Strong delete


    void addFavorite(User user, UUID directoryId);

    boolean removeFavorite(User user, UUID directoryId);

    List<Directory> findDirectoriesByIds(List<UUID> directoryIds, SortArguments sortArgs);

    List<Directory> findDirectoriesByIds(List<UUID> directoryIds);

    void loadDirectoryFavorites(List<UUID> directoryIds, UUID currentUserId);

    void loadRootDirsFileQuantity(List<UUID> directoryIds, UUID userToFilterId, UUID currentUserId);
}

