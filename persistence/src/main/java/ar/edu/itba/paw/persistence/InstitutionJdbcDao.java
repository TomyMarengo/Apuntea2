package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.InstitutionData;
import ar.edu.itba.paw.models.institutional.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class InstitutionJdbcDao implements InstitutionDao{
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Institution> ROW_MAPPER = (rs, rowNum)  ->
        new Institution(
                UUID.fromString(rs.getString(INSTITUTION_ID)),
                rs.getString(INSTITUTION_NAME)
        );

    @Autowired
    public InstitutionJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public InstitutionData getInstitutionData() {
        InstitutionData data = new InstitutionData();
        jdbcTemplate.query("SELECT * FROM InstitutionData", rs -> {
            Institution institution = new Institution(
                    UUID.fromString(rs.getString(INSTITUTION_ID)),
                    rs.getString(INSTITUTION_NAME)
            );
            Career career = new Career(
                    UUID.fromString(rs.getString(CAREER_ID)),
                    rs.getString(CAREER_NAME)
            );
            Subject subject = new Subject(
                    UUID.fromString(rs.getString(SUBJECT_ID)),
                    rs.getString(SUBJECT_NAME)
            );

            data.add(institution, career, subject);
        });

        return data;
    }
}
