package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;


@Repository
public class InstitutionJpaDao implements InstitutionDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public InstitutionData getInstitutionData() {
        // Fetch all institutions with their careers and subjects "eagerly"
        EntityGraph<Institution> entityGraph = em.createEntityGraph(Institution.class);
        entityGraph.addAttributeNodes(CAREERS_ATTR);
        entityGraph.addSubgraph(CAREERS_ATTR).addAttributeNodes(SUBJECTS_ATTR);

        final TypedQuery<Institution> query = em.createQuery("FROM Institution i", Institution.class);
        query.setHint(FETCH_GRAPH, entityGraph);
        return new InstitutionData(query.getResultList());
    }
}
