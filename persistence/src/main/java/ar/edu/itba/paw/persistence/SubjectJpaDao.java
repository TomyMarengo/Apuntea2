package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Subject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubjectJpaDao implements SubjectDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Subject> getSubjectById(UUID subjectId) {
//        return jdbcTemplate.query("SELECT s.subject_id, s.subject_name, s.root_directory_id FROM Subjects s WHERE s.subject_id = ?", ROW_MAPPER_WITH_ROOT_DIR, subjectId).stream().findFirst();
        return Optional.empty();
    }

    @Override
    public List<Subject> getSubjectsByCareerId(UUID careerId) {
//        return jdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name, sc.year, s.root_directory_id FROM Subjects s " +
//                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id WHERE sc.career_id = ? " +
//                "ORDER BY sc.year, s.subject_name", ROW_MAPPER_CAREER, careerId);
        return null;
    }

    @Override
    public UUID getSubjectByRootDirectoryId(UUID rootDirectoryId) {
        return em.createQuery("SELECT s.id FROM Subject s WHERE s.rootDirectoryId = :rootDirectoryId", UUID.class)
                .setParameter("rootDirectoryId", rootDirectoryId)
                .getSingleResult();
    }

    @Override
    public List<Subject> getSubjectsByCareerIdComplemented(UUID careerId) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(CAREER_ID, careerId);
//        return namedParameterJdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name FROM Subjects s " +
//                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id " +
//                "JOIN Careers c on sc.career_id = c.career_id " +
//                "WHERE c.institution_id IN (" +
//                "   SELECT target_c.institution_id FROM Careers target_c WHERE target_c.career_id = :career_id "+
//                ") AND s.subject_id NOT IN (" +
//                "   SELECT target_sc.subject_id FROM Subjects_Careers target_sc WHERE target_sc.career_id = :career_id" +
//                ") ORDER BY s.subject_name", args, ROW_MAPPER);
        return null;
    }

    @Override
    public UUID create(String name, UUID rootDirectoryId) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(NOTE_NAME, name);
//        args.addValue(ROOT_DIRECTORY_ID, rootDirectoryId);
//
//        KeyHolder holder = new GeneratedKeyHolder();
//        namedParameterJdbcTemplate.update("INSERT INTO Subjects (subject_name, root_directory_id) " +
//                "VALUES (:note_name, :root_directory_id)"
//                , args, holder, new String[]{SUBJECT_ID});
//        return (UUID) holder.getKeys().get(SUBJECT_ID);
        return null;
    }

    @Override
    public boolean delete(UUID subjectId) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(SUBJECT_ID, subjectId);
//        return namedParameterJdbcTemplate.update("DELETE FROM Subjects WHERE subject_id = :subject_id", args) == 1;
        return false;
    }

    @Override
    public boolean updateSubject(UUID subjectId, String name) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(SUBJECT_ID, subjectId);
//        args.addValue(SUBJECT_NAME, name);
//        return namedParameterJdbcTemplate.update("UPDATE Subjects SET subject_name = :subject_name WHERE subject_id = :subject_id", args) == 1;
        return false;
    }

    @Override
    public boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(SUBJECT_ID, subjectId);
//        args.addValue(CAREER_ID, careerId);
//        int count = namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) as n FROM Subjects_Careers sc " +
//                        "INNER JOIN Subjects s ON sc.subject_id = s.subject_id " +
//                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
//                        "WHERE sc.subject_id = :subject_id " +
//                        "AND c.institution_id != (SELECT c2.institution_id FROM Careers c2 WHERE c2.career_id = :career_id)",
//                args, Integer.class);
//        if (count == 0) {
//            jdbcSubjectsCareersInsert.execute(new HashMap<String, Object>() {{
//                put(SUBJECT_ID, subjectId);
//                put(CAREER_ID, careerId);
//                put(YEAR, year);
//            }});
//        }
//        return count == 0;
        return false;
    }

    @Override
    public boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(SUBJECT_ID, subjectId);
//        args.addValue(CAREER_ID, careerId);
//        args.addValue(YEAR, year);
//        return namedParameterJdbcTemplate.update("UPDATE Subjects_Careers SET year = :year WHERE subject_id = :subject_id AND career_id = :career_id", args) == 1;
        return false;
    }

    @Override
    public boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
//        MapSqlParameterSource args = new MapSqlParameterSource();
//        args.addValue(SUBJECT_ID, subjectId);
//        args.addValue(CAREER_ID, careerId);
//        return namedParameterJdbcTemplate.update("DELETE FROM Subjects_Careers WHERE subject_id = :subject_id AND career_id = :career_id", args) == 1;
        return false;
    }
}

