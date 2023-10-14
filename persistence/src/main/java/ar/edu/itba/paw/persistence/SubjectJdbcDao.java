package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class SubjectJdbcDao implements SubjectDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcSubjectsCareersInsert;

    private static final RowMapper<Subject> ROW_MAPPER = (rs, rowNum)  ->
        new Subject(
                UUID.fromString(rs.getString(SUBJECT_ID)),
                rs.getString(SUBJECT_NAME)
        );

    private static final RowMapper<Subject> ROW_MAPPER_WITH_ROOT_DIR = (rs, rowNum)  ->
            new Subject(
                    UUID.fromString(rs.getString(SUBJECT_ID)),
                    rs.getString(SUBJECT_NAME),
                    UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
            );

    private static final RowMapper<Subject> ROW_MAPPER_CAREER = (rs, rowNum)  ->
            new Subject(
                    UUID.fromString(rs.getString(SUBJECT_ID)),
                    rs.getString(SUBJECT_NAME),
                    UUID.fromString(rs.getString(ROOT_DIRECTORY_ID)),
                    rs.getInt(YEAR)
            );

    @Autowired
    public SubjectJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcSubjectsCareersInsert = new SimpleJdbcInsert(ds)
                .withTableName(SUBJECTS_CAREERS)
                .usingColumns(SUBJECT_ID, CAREER_ID, YEAR);
    }

    @Override
    public Optional<Subject> getSubjectById(UUID subjectId) {
        return jdbcTemplate.query("SELECT s.subject_id, s.subject_name, s.root_directory_id FROM Subjects s WHERE s.subject_id = ?", ROW_MAPPER_WITH_ROOT_DIR, subjectId).stream().findFirst();
    }

    @Override
    public List<Subject> getSubjects() {
        return jdbcTemplate.query("SELECT s.subject_id, s.subject_name FROM Subjects s", ROW_MAPPER);
    }

    @Override
    public List<Subject> getSubjectsByCareerId(UUID careerId) {
        return jdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name, sc.year, s.root_directory_id FROM Subjects s " +
                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id WHERE sc.career_id = ? " +
                "ORDER BY sc.year", ROW_MAPPER_CAREER, careerId);
    }

    @Override
    public List<Subject> getSubjectsByCareerIdComplemented(UUID careerId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(CAREER_ID, careerId);
        return namedParameterJdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name FROM Subjects s " +
                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id " +
                "JOIN Careers c on sc.career_id = c.career_id " +
                "WHERE c.institution_id IN (" +
                "   SELECT target_c.institution_id FROM Careers target_c WHERE target_c.career_id = :career_id "+
                ") AND s.subject_id NOT IN (" +
                "   SELECT target_sc.subject_id FROM Subjects_Careers target_sc WHERE target_sc.career_id = :career_id" +
                ") ORDER BY s.subject_name", args, ROW_MAPPER);
    }

    @Override
    public List<Subject> getSubjectsByInstitutionId(UUID institutionId) {
        return jdbcTemplate.query("SELECT DISTINCT s.subject_id, s.subject_name FROM Subjects s " +
                "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id JOIN Careers c on sc.career_id = c.career_id WHERE c.institution_id = ?", ROW_MAPPER, institutionId);
    }

    @Override
    public UUID create(String name, UUID rootDirectoryId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(ROOT_DIRECTORY_ID, rootDirectoryId);

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Subjects (subject_name, root_directory_id) " +
                "VALUES (:note_name, :root_directory_id)"
                , args, holder, new String[]{SUBJECT_ID});
        return (UUID) holder.getKeys().get(SUBJECT_ID);
    }

    @Override
    public boolean delete(UUID subjectId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_ID, subjectId);
        return namedParameterJdbcTemplate.update("DELETE FROM Subjects WHERE subject_id = :subject_id", args) == 1;
    }

    @Override
    public boolean linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(CAREER_ID, careerId);
        int count = namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) as n FROM Subjects_Careers sc " +
                        "INNER JOIN Subjects s ON sc.subject_id = s.subject_id " +
                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
                        "WHERE sc.subject_id = :subject_id " +
                        "AND c.institution_id != (SELECT c2.institution_id FROM Careers c2 WHERE c2.career_id = :career_id)",
                args, Integer.class);
        if (count == 0) {
            jdbcSubjectsCareersInsert.execute(new HashMap<String, Object>() {{
                put(SUBJECT_ID, subjectId);
                put(CAREER_ID, careerId);
                put(YEAR, year);
            }});
        }
        return count == 0;
    }

    @Override
    public boolean updateSubject(UUID subjectId, String name) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(SUBJECT_NAME, name);
        return namedParameterJdbcTemplate.update("UPDATE Subjects SET subject_name = :subject_name WHERE subject_id = :subject_id", args) == 1;
    }

    @Override
    public boolean updateSubjectCareer(UUID subjectId, UUID careerId, int year) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(CAREER_ID, careerId);
        args.addValue(YEAR, year);
        return namedParameterJdbcTemplate.update("UPDATE Subjects_Careers SET year = :year WHERE subject_id = :subject_id AND career_id = :career_id", args) == 1;
    }

    @Override
    public boolean unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(CAREER_ID, careerId);
        return namedParameterJdbcTemplate.update("DELETE FROM Subjects_Careers WHERE subject_id = :subject_id AND career_id = :career_id", args) == 1;
    }
}

