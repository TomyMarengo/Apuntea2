package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class SubjectJdbcDao implements SubjectDao {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Subject> ROW_MAPPER = (rs, rowNum)  ->
        new Subject(
                UUID.fromString(rs.getString(SUBJECT_ID)),
                rs.getString(SUBJECT_NAME)
        );

    @Autowired
    public SubjectJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Subject> getSubjects() {
        return jdbcTemplate.query("SELECT s.subject_id, s.subject_name FROM Subjects s", ROW_MAPPER);
    }

    public List<Subject> getSubjectsByCareerId(UUID careerId) {
        return jdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name FROM Subjects s " +
                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id WHERE sc.career_id = ?", ROW_MAPPER, careerId);
    }

    public List<Subject> getSubjectsByInstitutionId(UUID institutionId) {
        return jdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name FROM Subjects s " +
                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id JOIN Careers c on sc.career_id = c.career_id WHERE c.institution_id = ?", ROW_MAPPER, institutionId);
    }
}
