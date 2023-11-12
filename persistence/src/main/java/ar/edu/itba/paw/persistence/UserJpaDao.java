package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

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
    public List<User> getStudents(String query, UserStatus status, int pageNum, int pageSize) {
        String searchWord = escapeLikeString(query);
        String queryStr = "SELECT CAST(user_id AS VARCHAR(36)) FROM users u " +
                "WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') " +
                "AND (lower(u.username) LIKE lower(:searchWord) ESCAPE '!' OR lower(u.email) LIKE lower(:searchWord) ESCAPE '!') ORDER BY u.email";
        if (status != null) {
            queryStr += " AND u.status = :status";
        }

        Query nQuery = em.createNativeQuery(queryStr)
                .setMaxResults(pageSize)
                .setFirstResult((pageNum - 1) * pageSize)
                .setParameter("searchWord", searchWord);

        if (status != null) {
            nQuery.setParameter("status", status.toString().toUpperCase());
        }
        @SuppressWarnings("unchecked")
        List<UUID> userIds = ((List<String>) nQuery.getResultList()).stream().map(UUID::fromString).collect(Collectors.toList());


        if (userIds.isEmpty()) return Collections.emptyList();
        return em.createQuery("FROM User WHERE id IN :ids ORDER BY email", User.class)
                .setParameter("ids", userIds)
                .getResultList();
    }

    @Override
    public int getStudentsQuantity(String query, UserStatus status) {
        String searchWord = escapeLikeString(query);
        String queryStr = "SELECT COUNT(DISTINCT u.user_id) FROM users u " +
                "WHERE NOT EXISTS (SELECT 1 FROM User_Roles ur WHERE ur.user_id = u.user_id AND ur.role_name = 'ROLE_ADMIN') " +
                "AND (lower(u.username) LIKE lower(:searchWord) ESCAPE '!' OR lower(u.email) LIKE lower(:searchWord) ESCAPE '!')";
        if (status != null) {
            queryStr += " AND u.status = :status";
        }
        Query nQuery = em.createNativeQuery(queryStr)
                .setParameter("searchWord", searchWord);
        if (status != null) {
            nQuery.setParameter("status", status.toString().toUpperCase());
        }
        return ((Number) nQuery.getSingleResult()).intValue();

    }

    @Override
    public void create(final String email, final String password, final Career career, final String lang, final Collection<Role> roles){
        User user = new User.UserBuilder()
                .email(email)
                .password(password)
                .career(career)
                .locale(lang)
                .roles(roles)
                .status(UserStatus.ACTIVE)
                .build();
        em.persist(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return em.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst();
    }

    @Override
    public void updateProfilePicture(User user, Image img) {
        Image oldImg = user.getProfilePicture();
        if (oldImg != null) em.remove(oldImg);
        em.persist(img);
        user.setProfilePicture(img);
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
        if (user.isBanned()) return false; // TODO: check that the user is not disabled
        user.setStatus(UserStatus.BANNED);
        Ban ban = new Ban(user, admin, reason, endDate);
        em.persist(ban);
        return true;
    }
}
