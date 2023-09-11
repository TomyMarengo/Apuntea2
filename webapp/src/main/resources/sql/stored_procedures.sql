CREATE OR REPLACE FUNCTION create_or_update_review(
    _score IN INTEGER,
    _user_id IN UUID,
    _note_id IN UUID
)
RETURNS VOID AS '
BEGIN
    IF (SELECT COUNT(*) FROM reviews WHERE user_id = _user_id AND note_id = _note_id) = 0
    THEN
        INSERT INTO reviews (note_id, user_id, score) VALUES (_note_id, _user_id, _score);
    ELSE
        UPDATE reviews SET score = _score WHERE user_id = _user_id AND note_id = _note_id;
    END IF;
END;
' LANGUAGE plpgsql;
