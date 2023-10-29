package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.directory.Favorite;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DirectoryJpaDao implements DirectoryDao {
    @PersistenceContext
    private EntityManager em;

    private static final int RECURSION_LIMIT = 20;

    @Override
    public Directory create(String name, UUID parentId, User user, boolean visible, String iconColor) {
        Directory directory = new Directory.DirectoryBuilder()
                .name(name)
                .parentId(parentId)
                .user(user)
                .visible(visible)
                .iconColor(iconColor)
                .createdAt(LocalDateTime.now()) // TODO: See if it is possible to remove this
                .lastModifiedAt(LocalDateTime.now())
                .build();

        em.persist(directory);
        return directory;
    }

    @Override
    public Directory createRootDirectory(String name) {
        Directory directory = new Directory.DirectoryBuilder()
                .name(name)
                .visible(true)
                .createdAt(LocalDateTime.now()) // TODO: See if it is possible to remove this
                .lastModifiedAt(LocalDateTime.now())
                .build();

        em.persist(directory);
        return directory;
    }

    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId) {
        return em.createQuery("SELECT d FROM Directory d JOIN d.user u WHERE d.id = :directoryId AND (d.visible = true OR u.userId = :currentUserId)", Directory.class)
                .setParameter("directoryId", directoryId)
                .setParameter("currentUserId", currentUserId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<UUID> getDirectoryPathIds(UUID directoryId) {
        // Right now no user validation is needed, might change later

        return ((List<String>) em.createNativeQuery(
                "WITH RECURSIVE Ancestors(directory_id, parent_id, level) AS ( " +
                        "SELECT d.directory_id, d.parent_id, 0 as level FROM Directories d WHERE d.directory_id = :directoryId " +
                        "UNION " +
                        "SELECT d.directory_id, d.parent_id, a.level + 1 FROM Ancestors a INNER JOIN Directories d ON a.parent_id = d.directory_id " +
                        ") SELECT CAST(directory_id AS VARCHAR(36)) FROM Ancestors ORDER BY level DESC ")
                .setParameter("directoryId", directoryId)
                .setMaxResults(RECURSION_LIMIT)
                .getResultList()
        ).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    @Override
    public boolean delete(List<UUID> directoryIds, UUID currentUserId) {
        if (directoryIds.isEmpty()) return false;
        return em.createQuery("DELETE FROM Directory d WHERE d.id IN :directoryIds AND d.user.id = :currentUserId")
                .setParameter("directoryIds", directoryIds)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate() == directoryIds.size();
    }

    @Override
    public boolean delete(List<UUID> directoryIds) {
        if (directoryIds.isEmpty()) return false;
        return em.createQuery("DELETE FROM Directory d WHERE d.id IN :directoryIds")
                .setParameter("directoryIds", directoryIds)
                .executeUpdate() == directoryIds.size();
    }

    @Override
    public List<Directory> getFavorites(UUID userId) {
        return em.createQuery("SELECT d FROM Favorite f JOIN f.directory d JOIN d.user u WHERE f.user.id = :userId AND (d.visible = true OR u.id = :userId)", Directory.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void addFavorite(UUID userId, UUID directoryId) {
        Favorite fav = new Favorite(em.getReference(User.class, userId), em.getReference(Directory.class, directoryId));
        em.persist(fav);
    }

    @Override
    public boolean removeFavorite(UUID userId, UUID directoryId) {
        return em.createQuery("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.directory.id = :directoryId")
                .setParameter("userId", userId)
                .setParameter("directoryId", directoryId)
                .executeUpdate() == 1;
    }

    @Override
    public List<Directory> findDirectoriesByIds(List<UUID> directoryIds, User currentUser) {
        // TODO: Sort
        if (directoryIds.isEmpty()) return Collections.emptyList();
        List<Directory> directories = em.createQuery("FROM Directory d WHERE d.id IN :directoryIds ", Directory.class)
                .setParameter("directoryIds", directoryIds)
                .getResultList();
        // TODO: Optimize
        if (currentUser != null) {
            List<Directory> favorites = em.createQuery("SELECT f.directory FROM Favorite f WHERE f.user = :user AND f.directory.id IN :directoryIds", Directory.class)
                    .setParameter("user", currentUser)
                    .setParameter("directoryIds", directoryIds)
                    .getResultList();
            favorites.forEach(dir -> dir.setFavorite(true));
        }
        return directories;
    }

}