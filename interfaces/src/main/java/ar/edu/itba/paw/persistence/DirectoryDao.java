package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.search.SearchArguments;
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

    boolean delete(UUID directoryId, UUID currentUserId);

    boolean delete(UUID directoryId); /* Strong delete */

    boolean isFavorite(UUID userId, UUID directoryId);

    boolean addFavorite(UUID userId, UUID directoryId);

    boolean removeFavorite(UUID userId, UUID directoryId);

    /* Warning, this method does not check visibility! */
    List<Directory> findDirectoriesByIds(List<UUID> directoryIds, SortArguments sortArgs);

    List<Directory> findDirectoriesByIds(List<UUID> directoryIds);

    void loadRootDirsFileQuantity(List<UUID> directoryIds, UUID userToFilterId, UUID currentUserId);

    List<Directory> search(SearchArguments sa);
    List<Directory> navigate(SearchArguments sa, Boolean isRdir);

    int countSearchResults(SearchArguments sa);
    int countNavigationResults(SearchArguments sa, Boolean isRdir);
}

