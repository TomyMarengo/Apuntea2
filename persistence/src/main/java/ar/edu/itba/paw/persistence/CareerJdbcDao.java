package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CareerJdbcDao implements CareerDao{
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Career> ROW_MAPPER = (rs, rowNum)  ->
        new Career(
                UUID.fromString(rs.getString(CAREER_ID)),
                rs.getString(CAREER_NAME)
        );

    private static final RowMapper<Career> INDIVIDUAL_ROW_MAPPER = (rs, rowNum)  ->
            new Career(
                    UUID.fromString(rs.getString(CAREER_ID)),
                    rs.getString(CAREER_NAME),
                    UUID.fromString(rs.getString(INSTITUTION_ID))
            );

    @Autowired
    public CareerJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Career> getCareers() {
        return jdbcTemplate.query("SELECT * FROM Careers", ROW_MAPPER);
    }

    @Override
    public Optional<Career> getCareerById(UUID careerId) {
        return jdbcTemplate.query("SELECT c.career_id, c.career_name, c.institution_id FROM Careers c WHERE c.career_id = ? "
        , new Object[]{careerId}, INDIVIDUAL_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public int countCareersBySubjectId(UUID subjectId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Subjects_Careers WHERE subject_id = ?", new Object[]{subjectId}, Integer.class);
    }

}
