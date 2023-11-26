package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.search.SortArguments;
import ar.edu.itba.paw.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.models.NameConstants.*;

@Repository
public class DirectoryJpaDao implements DirectoryDao {
    @PersistenceContext
    private EntityManager em;

    private static final int RECURSION_LIMIT = 20;

    @Override
    public Directory create(String name, UUID parentId, User user, boolean visible, String iconColor) {
        Directory directory = new Directory.DirectoryBuilder()
                .name(name)
                .parent(em.getReference(Directory.class, parentId))
                .user(user)
                .visible(visible)
                .iconColor(iconColor)
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
                .lastModifiedAt(LocalDateTime.now())
                .build();

        em.persist(directory);
        return directory;
    }

    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId, UUID currentUserId) {
        Optional<Directory> d =
         em.createQuery("SELECT d FROM Directory d WHERE d.id = :directoryId AND (d.visible = true OR d.user.userId = :currentUserId)", Directory.class)
                .setParameter("directoryId", directoryId)
                .setParameter("currentUserId", currentUserId)
                .getResultList()
                .stream()
                .findFirst();
        return d;
    }

    @Override
    public List<Directory> getDirectoryPath(UUID directoryId) {
        // Right now no user validation is needed, might change later
        @SuppressWarnings("unchecked")
        List<UUID> directoryIds = ((List<String>) em.createNativeQuery(
                "WITH RECURSIVE Ancestors(directory_id, parent_id, level) AS ( " +
                        "SELECT d.directory_id, d.parent_id, 0 as level FROM Directories d WHERE d.directory_id = :directoryId " +
                        "UNION " +
                        "SELECT d.directory_id, d.parent_id, a.level + 1 FROM Ancestors a INNER JOIN Directories d ON a.parent_id = d.directory_id " +
                        ") SELECT CAST(directory_id AS VARCHAR(36)) FROM Ancestors ORDER BY level DESC ")
                .setParameter("directoryId", directoryId)
                .setMaxResults(RECURSION_LIMIT)
                .getResultList()
        ).stream().map(UUID::fromString).collect(Collectors.toList());
        return findDirectoriesByIds(directoryIds);
    }

    @Override
    public Optional<Directory> getDirectoryRoot(UUID directoryId) {
        // Right now no user validation is needed, might change later
        @SuppressWarnings("unchecked")
        Optional<UUID> maybeRootDirId = ((Optional<String>) em.createNativeQuery(
                        "WITH RECURSIVE Ancestors(directory_id, parent_id) AS ( " +
                                "SELECT d.directory_id, d.parent_id FROM Directories d WHERE d.directory_id = :directoryId " +
                                "UNION " +
                                "SELECT d.directory_id, d.parent_id FROM Ancestors a INNER JOIN Directories d ON a.parent_id = d.directory_id " +
                                ") SELECT CAST(directory_id AS VARCHAR(36)) FROM Ancestors WHERE parent_id IS NULL")
                .setParameter("directoryId", directoryId)
                .setMaxResults(RECURSION_LIMIT)
                .getResultList().stream().findFirst()
        ).map(UUID::fromString);
        return maybeRootDirId.map(id -> em.find(Directory.class, id));
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
    public void addFavorite(User user, UUID directoryId) {
        user.getDirectoryFavorites().add(em.getReference(Directory.class, directoryId));
    }

    @Override
    public boolean removeFavorite(User user, UUID directoryId) {
        return user.getDirectoryFavorites().remove(em.getReference(Directory.class,directoryId));
    }

    @Override
    public List<Directory> findDirectoriesByIds(List<UUID> directoryIds, SortArguments sortArgs) {
        // TODO: Reduce amount of joins if necessary
        if (directoryIds.isEmpty()) return Collections.emptyList();
        return em.createQuery(String.format("SELECT d FROM Directory d LEFT JOIN d.parent p LEFT JOIN p.subject s LEFT JOIN d.user u WHERE d.id IN :directoryIds ORDER BY d.%s %s", DaoUtils.SORTBY_CAMELCASE.getOrDefault(sortArgs.getSortBy(), NAME), sortArgs.isAscending()? "ASC" : "DESC"), Directory.class)
                .setParameter("directoryIds", directoryIds)
                .getResultList();
    }

    @Override
    public void loadDirectoryFavorites(List<UUID> directoryIds, UUID currentUserId) {
        if (currentUserId == null) return;
        List<Directory> favorites = em.createQuery("SELECT d FROM User u JOIN u.directoryFavorites d WHERE d.directoryId IN :directoryIds AND u.userId = :userId", Directory.class)
                .setParameter("userId", currentUserId)
                .setParameter("directoryIds", directoryIds)
                .getResultList();
        favorites.forEach(dir -> dir.setFavorite(true));
    }

    @Override
    public List<Directory> findDirectoriesByIds(List<UUID> directoryIds) {
        return findDirectoriesByIds(directoryIds, new SortArguments(SortArguments.SortBy.DATE, true));
    }

    @Override
    public void loadRootDirsFileQuantity(List<UUID> directoryIds, UUID userToFilterId, UUID currentUserId) {
        List<Object[]> list = em.createQuery("SELECT d, (SELECT COUNT(n.noteId) FROM Note n JOIN n.subject s WHERE (n.user.id = :currentUserId OR n.visible = true) AND s.rootDirectory = d AND n.user.id = :userId) as qtyFiles FROM Directory d WHERE d.id IN :directoryIds", Object[].class)
                .setParameter("directoryIds", directoryIds)
                .setParameter("userId", userToFilterId)
                .setParameter("currentUserId", currentUserId)
                .getResultList();

        list.forEach(o -> ((Directory) o[0]).setQtyFiles(((Long) o[1]).intValue()));
    }

}