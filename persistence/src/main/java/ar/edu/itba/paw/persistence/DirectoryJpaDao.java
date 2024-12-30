package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.search.SortArguments;
import ar.edu.itba.paw.models.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.models.NameConstants.*;

@Repository
public class DirectoryJpaDao implements DirectoryDao {
    @PersistenceContext
    private EntityManager em;

    private static final int RECURSION_LIMIT = 20;

    private	static final Logger LOGGER = LoggerFactory.getLogger(DirectoryJpaDao.class);

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
    public boolean delete(UUID directoryId, UUID currentUserId) {
        return em.createQuery("DELETE FROM Directory d WHERE d.id = :directoryId AND d.user.id = :currentUserId")
                .setParameter("directoryId", directoryId)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean delete(UUID directoryId) {
        return em.createQuery("DELETE FROM Directory d WHERE d.id = :directoryId")
                .setParameter("directoryId", directoryId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean isFavorite(UUID userId, UUID directoryId) {
        return em.createNativeQuery("SELECT 1 FROM Directory_Favorites WHERE user_id = :userId AND directory_id = :directoryId")
                .setParameter("userId", userId)
                .setParameter("directoryId", directoryId)
                .getResultList().size() == 1;
    }

    @Override
    public boolean addFavorite(UUID userId, UUID directoryId) {
        return em.createNativeQuery("INSERT INTO Directory_Favorites (user_id, directory_id) SELECT :userId, :directoryId WHERE NOT EXISTS (SELECT 1 FROM Directory_Favorites WHERE user_id = :userId AND directory_id = :directoryId)")
                .setParameter("userId", userId)
                .setParameter("directoryId", directoryId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean removeFavorite(UUID userId, UUID directoryId) {
        return em.createNativeQuery("DELETE FROM Directory_Favorites WHERE user_id = :userId AND directory_id = :directoryId")
                .setParameter("userId", userId)
                .setParameter("directoryId", directoryId)
                .executeUpdate() == 1;
    }

    @Override
    public List<Directory> findDirectoriesByIds(List<UUID> directoryIds, SortArguments sortArgs) {
        if (directoryIds.isEmpty()) return Collections.emptyList();
        TypedQuery<Directory> query = em.createQuery(String.format("SELECT d FROM Directory d LEFT JOIN d.parent p LEFT JOIN p.subject s LEFT JOIN d.user u WHERE d.id IN :directoryIds ORDER BY d.%s %s", DaoUtils.SORTBY_CAMELCASE.getOrDefault(sortArgs.getSortBy(), NAME), sortArgs.isAscending()? "ASC" : "DESC"), Directory.class)
                .setParameter("directoryIds", directoryIds);
        return query.getResultList();
    }

    /*@Override
    public void loadDirectoryFavorites(List<UUID> directoryIds, UUID currentUserId) {
        if (currentUserId == null) return;
        List<Directory> favorites = em.createQuery("SELECT d FROM User u JOIN u.directoryFavorites d WHERE d.directoryId IN :directoryIds AND u.userId = :userId", Directory.class)
                .setParameter("userId", currentUserId)
                .setParameter("directoryIds", directoryIds)
                .getResultList();
        favorites.forEach(dir -> dir.setFavorite(true));
    }*/

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

    @Override
    public List<Directory> search(SearchArguments sa) {
        SortArguments sortArgs = sa.getSortArguments();

        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT DISTINCT CAST(id as VARCHAR(36)) as id, ")
                .append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME))
                .append(" FROM Normalized_Directories t ")
                .append("INNER JOIN Subjects s ON t.parent_id = s.root_directory_id ")
                .append("INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id ")
                .append("INNER JOIN Careers c ON sc.career_id = c.career_id ")
                .append("INNER JOIN Institutions i ON c.institution_id = i.institution_id ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id ")
                .append("WHERE TRUE ");
        DaoUtils.applyInstitutionalFilters(queryCreator, sa);
        return getSearchResults(queryCreator, sa);
    }

    @Override
    public int countSearchResults(SearchArguments sa) {
        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT COUNT(DISTINCT id) FROM Normalized_Directories t ")
                .append("INNER JOIN Subjects s ON t.parent_id = s.root_directory_id ")
                .append("INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id ")
                .append("INNER JOIN Careers c ON sc.career_id = c.career_id ")
                .append("INNER JOIN Institutions i ON c.institution_id = i.institution_id ")
                .append("INNER JOIN Users u ON t.user_id = u.user_id ")
                .append("WHERE TRUE ");
        DaoUtils.applyInstitutionalFilters(queryCreator, sa);
        DaoUtils.applyGeneralFilters(queryCreator, sa);

        Query query = em.createNativeQuery(queryCreator.createQuery());
        queryCreator.getParams().forEach(query::setParameter);

        return ((BigInteger)query.getSingleResult()).intValue();
    }

    @Override
    public List<Directory> navigate(SearchArguments sa, Boolean isRdir) {
        SortArguments sortArgs = sa.getSortArguments();

        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT DISTINCT CAST(id as VARCHAR(36)) as id, ")
                .append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME))
                .append(" FROM Normalized_Directories t ")
                .appendIfPresent(sa.getFavBy(), "INNER JOIN Directory_Favorites df ON t.id = df.directory_id AND df.user_id = :favBy ")
                .append("LEFT JOIN Users u ON t.user_id = u.user_id WHERE TRUE ");//u.user_id IS ").append(isRdir? "NULL " : "NOT NULL ");
        if (isRdir != null)
            queryCreator.append("u.user_id IS ").append(isRdir? "NULL " : "NOT NULL ");
        queryCreator.addConditionIfPresent(PARENT_ID, "=", "AND", sa.getParentId());
        sa.getFavBy().ifPresent(favBy -> queryCreator.addParameter(FAV_BY, favBy));
        return getSearchResults(queryCreator, sa);
    }

    @Override
    public int countNavigationResults(SearchArguments sa, Boolean isRdir) {
        DaoUtils.QueryCreator queryCreator = new DaoUtils.QueryCreator("SELECT COUNT(DISTINCT CAST(id as VARCHAR(36))) ")
                .append(" FROM Normalized_Directories t ")
                .appendIfPresent(sa.getFavBy(), "INNER JOIN Directory_Favorites df ON t.id = df.directory_id AND df.user_id = :favBy ")
                .append("LEFT JOIN Users u ON t.user_id = u.user_id WHERE TRUE ");
        if (isRdir != null)
            queryCreator.append("u.user_id IS ").append(isRdir? "NULL " : "NOT NULL ");
        queryCreator.addConditionIfPresent(PARENT_ID, "=", "AND", sa.getParentId());
        sa.getFavBy().ifPresent(favBy -> queryCreator.addParameter(FAV_BY, favBy));

        DaoUtils.applyGeneralFilters(queryCreator, sa);

        Query query = em.createNativeQuery(queryCreator.createQuery());
        queryCreator.getParams().forEach(query::setParameter);

        return ((BigInteger)query.getSingleResult()).intValue();
    }


    private List<Directory> getSearchResults(DaoUtils.QueryCreator queryCreator, SearchArguments sa){
        SortArguments sortArgs = sa.getSortArguments();
        DaoUtils.applyGeneralFilters(queryCreator, sa);

        queryCreator.append("ORDER BY ").append(DaoUtils.SORTBY.getOrDefault(sortArgs.getSortBy(), NAME)).append(sortArgs.isAscending() ? "" : " DESC ");

        Query query = em.createNativeQuery(queryCreator.createQuery())
                .setFirstResult(sa.getPageSize() * (sa.getPage() - 1))
                .setMaxResults(sa.getPageSize());

        queryCreator.getParams().forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<UUID> directoryIds = ((List<Object[]>) query.getResultList()).stream().map(r -> UUID.fromString(r[0].toString())).collect(Collectors.toList());

        return findDirectoriesByIds(directoryIds, sortArgs);
        // TODO: Polemicardo in the bar
        // sa.getCurrentUserId().ifPresent(uId -> directoryDao.loadDirectoryFavorites(directoryIds, uId));
    }

}