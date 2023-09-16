package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Career;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class CareerJdbcDao implements CareerDao{
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Career> ROW_MAPPER = (rs, rowNum)  ->
        new Career(
                UUID.fromString(rs.getString(CAREER_ID)),
                rs.getString(CAREER_NAME)
        );

    @Autowired
    public CareerJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Career> getCareers() {
        return jdbcTemplate.query("SELECT * FROM Careers", ROW_MAPPER);
    }

    public List<Career> getCareersByInstitutionId(UUID institutionId) {
        return jdbcTemplate.query("SELECT * FROM Careers WHERE institution_id = ?", ROW_MAPPER, institutionId);
    }
}
