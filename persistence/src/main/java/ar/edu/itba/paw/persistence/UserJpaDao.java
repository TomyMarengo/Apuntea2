package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static ar.edu.itba.paw.persistence.DaoUtils.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
class UserJpaDao implements UserDao {
    private final Logger LOGGER = LoggerFactory.getLogger(UserJpaDao.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getUsers(String query, UserStatus status, UUID followedBy, int pageNum, int pageSize) {
        final String searchWord = escapeLikeString(query);
        final StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT CAST(user_id AS VARCHAR(36)) FROM users u " );
        if (followedBy != null)
            queryBuilder.append("JOIN Follows f ON u.user_id = f.followed_id AND f.follower_id = :followedBy ");

        //.append("WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') ")
        queryBuilder.append("WHERE (lower(u.username) LIKE lower(:searchWord) ESCAPE '!' OR lower(u.email) LIKE lower(:searchWord) ESCAPE '!') ")
                .append(status != null ? " AND u.status = :status" : "")
                .append(" ORDER BY u.email");

        Query nQuery = em.createNativeQuery(queryBuilder.toString())
                .setMaxResults(pageSize)
                .setFirstResult((pageNum - 1) * pageSize)
                .setParameter("searchWord", searchWord);

        if (status != null)
            nQuery.setParameter("status", status.toString().toUpperCase());

        if (followedBy != null)
            nQuery.setParameter("followedBy", followedBy);
        @SuppressWarnings("unchecked")
        List<UUID> userIds = ((List<String>) nQuery.getResultList()).stream().map(UUID::fromString).collect(Collectors.toList());


        if (userIds.isEmpty()) return Collections.emptyList();
        return em.createQuery("FROM User WHERE id IN :ids ORDER BY email", User.class)
                .setParameter("ids", userIds)
                .getResultList();
    }

    @Override
    public int getUsersQuantity(String query, UserStatus status, UUID followedBy) {
        final String searchWord = escapeLikeString(query);
        final StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT COUNT(user_id) FROM users u " );
        if (followedBy != null)
            queryBuilder.append("JOIN Follows f ON u.user_id = f.followed_id AND f.follower_id = :followedBy ");

        //.append("WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') ")
        queryBuilder.append("WHERE (lower(u.username) LIKE lower(:searchWord) ESCAPE '!' OR lower(u.email) LIKE lower(:searchWord) ESCAPE '!') ")
                .append(status != null ? " AND u.status = :status" : "");

        Query nQuery = em.createNativeQuery(queryBuilder.toString())
                .setParameter("searchWord", searchWord);

        if (status != null)
            nQuery.setParameter("status", status.toString().toUpperCase());

        if (followedBy != null)
            nQuery.setParameter("followedBy", followedBy);

        return ((Number) nQuery.getSingleResult()).intValue();

    }

    @Override
    public UUID create(final String email, final String password, final Career career, final String lang, final Collection<Role> roles){
        User user = new User.UserBuilder()
                .email(email)
                .password(password)
                .career(career)
                .locale(lang)
                .roles(roles)
                .status(UserStatus.ACTIVE)
                .build();
        em.persist(user);
        return user.getUserId();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public boolean follow(UUID followerId, UUID followedId) {
        return em.createNativeQuery("INSERT INTO Follows (followed_id, follower_id) SELECT :followedId, :followerId WHERE NOT EXISTS (SELECT 1 FROM Follows WHERE followed_id = :followedId AND follower_id = :followerId)")
                .setParameter("followedId", followedId)
                .setParameter("followerId", followerId)
                .executeUpdate() == 1;
    }

    @Override
    public boolean unfollow(UUID followerId, UUID followedId) {
        return em.createNativeQuery("DELETE FROM Follows WHERE followed_id = :followedId AND follower_id = :followerId")
                .setParameter("followedId", followedId)
                .setParameter("followerId", followerId)
                .executeUpdate() == 1;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return em.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst();
    }

    @Override
    public UUID updateProfilePicture(User user, Image img) {
        Image oldImg = user.getProfilePicture();
        if (oldImg != null) em.remove(oldImg);
        em.persist(img);
        user.setProfilePicture(img);
        return img.getImageId();
    }

    @Override
    public boolean isFollowing(UUID followerId, UUID followedId) {
        return em.createNativeQuery("SELECT 1 FROM Follows WHERE followed_id = :followedId AND follower_id = :followerId")
                .setParameter("followedId", followedId)
                .setParameter("followerId", followerId)
                .getResultList().size() == 1;
    }

    @Override
    public int unbanUsers() {
        return em.createQuery("UPDATE User u SET status = 'ACTIVE' WHERE status = 'BANNED' " +
                        "AND NOT EXISTS (SELECT 1 FROM Ban b WHERE b.banId.user.userId = u.userId AND b.endDate > now())")
                .executeUpdate();
    }

    @Override
    public boolean unbanUser(User user) {
        if (!user.isBanned()) return false;
        user.setStatus(UserStatus.ACTIVE);
        return true;
    }

    @Override
    public boolean banUser(User user, User admin, LocalDateTime endDate, String reason) {
        if (user.isBanned()) return false;
        user.setStatus(UserStatus.BANNED);
        Ban ban = new Ban(user, admin, reason, endDate);
        em.persist(ban);
        return true;
    }

    @Override
    public float getAvgScore(UUID userId) {
        return em.createQuery("SELECT ROUND(COALESCE(AVG(r.score), 0), 1) FROM Review r JOIN r.note n WHERE n.user.id = :userId", Double.class)
                .setParameter("userId", userId)
                .getSingleResult().floatValue();
    }

    @Override
    public Optional<byte[]> getProfilePicture(UUID pictureId) {
        return em.createQuery("SELECT i.picture FROM Image i WHERE i.imageId = :pictureId", byte[].class)
                .setParameter("pictureId", pictureId)
                .getResultList().stream().findFirst();
    }

}
