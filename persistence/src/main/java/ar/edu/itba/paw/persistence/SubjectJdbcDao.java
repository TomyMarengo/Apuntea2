package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class SubjectJdbcDao implements SubjectDao {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Subject> ROW_MAPPER = (rs, rowNum)  ->
        new Subject(
                UUID.fromString(rs.getString("subject_id")),
                rs.getString("name")
        );

    @Autowired
    public SubjectJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Subject> getSubjects() {
        return jdbcTemplate.query("SELECT * FROM Subjects", ROW_MAPPER);
    }

    public List<Subject> getSubjectsByCareerId(UUID careerId) {
        return jdbcTemplate.query("SELECT * FROM Subjects WHERE career_id = ?", ROW_MAPPER, careerId);
    }

    public List<Subject> getSubjectsByInstitutionId(UUID institutionId) {
        return jdbcTemplate.query("SELECT * FROM Subjects s JOIN Careers c on s.career_id = c.career_id WHERE c.institution_id = ?", ROW_MAPPER, institutionId);
    }
}
