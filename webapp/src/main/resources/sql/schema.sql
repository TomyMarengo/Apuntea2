--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--SPRINT 1

CREATE TABLE IF NOT EXISTS Institutions
(
  institution_id uuid NOT NULL DEFAULT gen_random_uuid(),
  institution_name character varying,
  acronym character varying(10),
  CONSTRAINT "PK_institutions" PRIMARY KEY (institution_id),
  CONSTRAINT "UQ_institutions_name" UNIQUE (institution_name)
);

CREATE TABLE IF NOT EXISTS Careers
(
  career_id uuid NOT NULL DEFAULT gen_random_uuid(),
  institution_id uuid NOT NULL,
  career_name character varying(30),
  CONSTRAINT "PK_careers" PRIMARY KEY (career_id),
  CONSTRAINT "FK_careers_institutions" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
);

CREATE TABLE IF NOT EXISTS Users
(
  user_id uuid NOT NULL DEFAULT gen_random_uuid(),
  username character varying(30),
  email character varying(320) NOT NULL,
  password character varying,
  first_name character varying,
  last_name character varying,
  institution_id uuid,
  biography text,
  CONSTRAINT "PK_users" PRIMARY KEY (user_id),
  CONSTRAINT "UQ_users_email" UNIQUE (email),
  CONSTRAINT "UQ_users_username" UNIQUE (username),
  CONSTRAINT "FK_users_institutions" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
);

CREATE TABLE IF NOT EXISTS Directories
(
  directory_id uuid NOT NULL DEFAULT gen_random_uuid(),
  directory_name character varying,
  parent_id uuid,
  user_id uuid,
  CONSTRAINT "PK_directories" PRIMARY KEY (directory_id),
  CONSTRAINT "UQ_directories" UNIQUE (directory_name, user_id, parent_id),
  CONSTRAINT "FK_directories_users" FOREIGN KEY (user_id) REFERENCES Users (user_id),
  CONSTRAINT "FK_directories_directories" FOREIGN KEY (parent_id) REFERENCES Directories (directory_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Subjects
(
  subject_id uuid NOT NULL DEFAULT gen_random_uuid(),
  subject_name character varying,
  root_directory_id uuid,
  CONSTRAINT "PK_subjects" PRIMARY KEY (subject_id),
  CONSTRAINT "FK_subjects_directories" FOREIGN KEY (root_directory_id) REFERENCES Directories (directory_id)
);

CREATE TABLE IF NOT EXISTS Subjects_Careers
(
  subject_id uuid NOT NULL,
  career_id uuid NOT NULL,
  CONSTRAINT "PK_subjects_careers" PRIMARY KEY (subject_id, career_id),
  CONSTRAINT "FK_subjects_careers_careers" FOREIGN KEY (career_id) REFERENCES Careers (career_id) ON DELETE CASCADE,
  CONSTRAINT "FK_subjects_careers_subjects" FOREIGN KEY (subject_id) REFERENCES Subjects (subject_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Notes
(
  note_id uuid NOT NULL DEFAULT gen_random_uuid(),
  note_name varchar NOT NULL,
  user_id uuid,
  file bytea NOT NULL,
  category varchar CHECK (category IN ('PRACTICE', 'THEORY', 'EXAM', 'OTHER')),
  subject_id uuid,
  parent_id uuid,
  created_at timestamp DEFAULT now(),
  CONSTRAINT "PK_notes" PRIMARY KEY (note_id),
  CONSTRAINT "UQ_notes" UNIQUE (note_name, user_id, parent_id),
  CONSTRAINT "FK_notes_directories" FOREIGN KEY (parent_id) REFERENCES Directories (directory_id) ON DELETE CASCADE,
  CONSTRAINT "FK_notes_subjects" FOREIGN KEY (subject_id) REFERENCES Subjects (subject_id),
  CONSTRAINT "FK_notes_users" FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

CREATE TABLE IF NOT EXISTS Reviews
(
  note_id uuid NOT NULL,
  user_id uuid NOT NULL,
  score smallint NOT NULL,
  content text,
  CONSTRAINT "PK_reviews" PRIMARY KEY (note_id, user_id),
  CONSTRAINT "FK_reviews_notes" FOREIGN KEY (note_id) REFERENCES Notes (note_id) ON DELETE CASCADE,
  CONSTRAINT "FK_reviews_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);


-----------------------------------------------------------------------------------------------------------
--SPRINT 2

CREATE TABLE IF NOT EXISTS User_Roles
(
  user_id uuid NOT NULL,
  role_name character varying(30) NOT NULL,
  CONSTRAINT "PK_user_roles" PRIMARY KEY (user_id, role_name),
  CONSTRAINT "FK_user_roles_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);

--run this if user_roles is empty
--INSERT INTO User_Roles (user_id, role_name) SELECT user_id, 'ROLE_USER' FROM Users AND NOT EXISTS (SELECT * FROM User_Roles WHERE role_name = 'ROLE_USER');

ALTER TABLE Notes
    ADD COLUMN IF NOT EXISTS visible boolean DEFAULT true NOT NULL,
    ADD COLUMN IF NOT EXISTS file_type character varying(10) NULL,
    ADD COLUMN IF NOT EXISTS last_modified_at timestamp DEFAULT now() NOT NULL;

--update all null types to pdf for retro-compatibility
UPDATE Notes SET file_type = 'pdf' WHERE file_type IS NULL;
ALTER TABLE Notes
    ALTER COLUMN file_type SET NOT NULL,
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE Directories
    ADD COLUMN IF NOT EXISTS visible boolean DEFAULT true NOT NULL,
    ADD COLUMN IF NOT EXISTS created_at timestamp DEFAULT now() NOT NULL,
    ADD COLUMN IF NOT EXISTS last_modified_at timestamp DEFAULT now() NOT NULL,
    ADD COLUMN IF NOT EXISTS icon_color character varying(7) DEFAULT 'BBBBBB' NOT NULL,
    -- For root directory validation
    DROP CONSTRAINT IF EXISTS "CK_root_directories",
    ADD CONSTRAINT "CK_root_directories" CHECK ((parent_id IS NULL AND user_id IS NULL) OR (parent_id IS NOT NULL AND user_id IS NOT NULL));


ALTER TABLE Users
    ADD COLUMN IF NOT EXISTS profile_picture bytea,
    ADD COLUMN IF NOT EXISTS locale character varying(5) DEFAULT 'en' NOT NULL,
    ADD COLUMN IF NOT EXISTS career_id UUID REFERENCES Careers (career_id),
    DROP CONSTRAINT IF EXISTS "FK_users_institutions",
    DROP COLUMN IF EXISTS institution_id;

--create view for notes and directories
CREATE OR REPLACE VIEW Navigation AS (
    SELECT t.*, u.email
    FROM (
       SELECT n.note_id as id, n.parent_id, n.user_id, n.note_name as name,  n.category, n.created_at, n.last_modified_at, n.visible, n.file_type, NULL as icon_color, COALESCE(AVG(r.score), 0) AS avg_score
       FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id GROUP BY n.note_id
       UNION
       SELECT d.directory_id, d.parent_id, d.user_id, d.directory_name, 'directory', d.created_at, d.last_modified_at, d.visible, NULL, d.icon_color, 0
       FROM Directories d
     ) as t
     INNER JOIN Users u ON t.user_id = u.user_id
);

CREATE OR REPLACE VIEW Search AS (
  SELECT t.*,
         i.institution_id, i.institution_name, c.career_id, c.career_name, s.subject_name, s.root_directory_id, u.email
      FROM (
         SELECT n.note_id as id, n.parent_id, n.user_id, n.subject_id, n.note_name as name, n.category, n.created_at, n.last_modified_at, COALESCE(AVG(r.score), 0) AS avg_score, n.visible, n.file_type, NULL as icon_color
         FROM Notes n LEFT JOIN Reviews r ON n.note_id = r.note_id GROUP BY n.note_id
         UNION
         SELECT d.directory_id, d.parent_id, d.user_id, s.subject_id, d.directory_name, 'directory', d.created_at, d.last_modified_at, 0, d.visible, NULL, d.icon_color
         FROM Directories d INNER JOIN Subjects s ON d.parent_id = s.root_directory_id
     ) AS t
     INNER JOIN Subjects s ON t.subject_id = s.subject_id
     INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id
     INNER JOIN Careers c ON sc.career_id = c.career_id
     INNER JOIN Institutions i ON c.institution_id = i.institution_id
     INNER JOIN Users u ON t.user_id = u.user_id
);

CREATE OR REPLACE VIEW InstitutionData AS (
    SELECT i.institution_id, i.institution_name, c.career_id, c.career_name, s.subject_id, s.subject_name
    FROM Institutions i
    INNER JOIN Careers c ON i.institution_id = c.institution_id
    INNER JOIN Subjects_Careers sc ON c.career_id = sc.career_id
    INNER JOIN Subjects s ON sc.subject_id = s.subject_id
);

-----------------------------------------------------------------------------------------------------------
--SPRINT 3
CREATE TABLE IF NOT EXISTS Images
(
  image_id uuid NOT NULL DEFAULT gen_random_uuid(),
  image bytea NOT NULL,
  CONSTRAINT "PK_images" PRIMARY KEY (image_id)
);

ALTER TABLE Users
    ADD COLUMN IF NOT EXISTS profile_picture_id uuid,
    DROP CONSTRAINT IF EXISTS "FK_users_images",
    ADD CONSTRAINT "FK_users_images" FOREIGN KEY (profile_picture_id) REFERENCES Images (image_id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS status character varying(10) DEFAULT 'ACTIVE' NOT NULL,
    DROP CONSTRAINT IF EXISTS "CK_users_status",
    ADD CONSTRAINT "CK_users_status" CHECK (status IN ('ACTIVE', 'BANNED', 'DELETED')),
    DROP COLUMN IF EXISTS profile_picture;

CREATE TABLE IF NOT EXISTS Favorites
(
  user_id uuid NOT NULL,
  directory_id uuid NOT NULL,
  CONSTRAINT "PK_favorites" PRIMARY KEY (user_id, directory_id),
  CONSTRAINT "FK_favorites_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE,
  CONSTRAINT "FK_favorites_directories" FOREIGN KEY (directory_id) REFERENCES Directories (directory_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Bans
(
  user_id uuid NOT NULL,
  admin_id uuid NOT NULL,
  start_date timestamp NOT NULL DEFAULT now(),
  end_date timestamp NOT NULL,
  reason text,
  CONSTRAINT "PK_bans" PRIMARY KEY (user_id, admin_id, start_date),
  CONSTRAINT "FK_bans_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE,
  CONSTRAINT "FK_bans_admins" FOREIGN KEY (admin_id) REFERENCES Users (user_id) ON DELETE CASCADE
);

ALTER TABLE Subjects_Careers ADD COLUMN IF NOT EXISTS year smallint NOT NULL DEFAULT 1;

CREATE TABLE IF NOT EXISTS Verification_Codes
(
  user_id uuid NOT NULL,
  code character varying(6) NOT NULL,
  expires_at timestamp NOT NULL,
  CONSTRAINT "PK_verification_codes" PRIMARY KEY (user_id, code, expires_at),
  CONSTRAINT "FK_verification_codes_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);

ALTER TABLE Reviews ADD COLUMN IF NOT EXISTS created_at timestamp DEFAULT now() NOT NULL;

-----------------------------------------------------------------------------------------------------------
--SPRINT 4

ALTER TABLE Notes ADD COLUMN IF NOT EXISTS file bytea;

CREATE TABLE IF NOT EXISTS Note_Files
(
    note_id uuid NOT NULL,
    file bytea NOT NULL,
    CONSTRAINT "PK_note_files" PRIMARY KEY (note_id),
    CONSTRAINT "FK_note_files_notes" FOREIGN KEY (note_id) REFERENCES Notes (note_id) ON DELETE CASCADE
);

INSERT INTO Note_Files (file, note_id) SELECT file, note_id FROM Notes WHERE file IS NOT NULL;

ALTER TABLE Notes DROP COLUMN IF EXISTS file;

