package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.search.Searchable;
import ar.edu.itba.paw.models.search.SortArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.*;

import static ar.edu.itba.paw.persistence.DaoUtils.*;
import static ar.edu.itba.paw.models.NameConstants.*;

@Repository
public class SearchJpaDao implements SearchDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UUID> findByName(UUID parentId, String name, UUID currentUserId) {
        Query q = (em.createNativeQuery("SELECT CAST(n.id AS VARCHAR(36)) FROM Navigation n WHERE n.name = :name AND n.parent_id = :parentId AND n.user_id = :currentUserId")
                .setParameter("name", name)
                .setParameter("parentId", parentId)
                .setParameter("currentUserId", currentUserId));
        return q.getResultList()
                .stream()
                .findFirst().map(o -> UUID.fromString((String) o));
    }

    @Override
    public int countChildren(UUID parentId) {
        return ((BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM Navigation WHERE parent_id = :parentId")
                .setParameter("parentId", parentId)
                .getSingleResult()).intValue();
    }

}
