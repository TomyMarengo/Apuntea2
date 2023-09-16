package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Institution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class InstitutionJdbcDao implements InstitutionDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Institution> ROW_MAPPER = (rs, rowNum)  ->
        new Institution(
                UUID.fromString(rs.getString(INSTITUTION_ID)),
                rs.getString(INSTITUTION_NAME)
        );

    @Autowired
    public InstitutionJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(INSTITUTIONS)
                .usingGeneratedKeyColumns(INSTITUTION_ID);
    }

    @Override
    public List<Institution> getInstitutions() {
        return jdbcTemplate.query("SELECT * FROM Institutions", ROW_MAPPER);
    }
}
