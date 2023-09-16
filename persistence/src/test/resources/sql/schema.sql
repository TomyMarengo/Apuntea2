--CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS Institutions
(
  institution_id uuid NOT NULL DEFAULT uuid(),
  institution_name character varying(100),
  acronym character varying(100),
  CONSTRAINT "PK_institutions" PRIMARY KEY (institution_id),
  CONSTRAINT "UQ_institutions_name" UNIQUE (institution_name)
);

CREATE TABLE IF NOT EXISTS Careers
(
  career_id uuid NOT NULL DEFAULT uuid(),
  institution_id uuid NOT NULL,
  career_name character varying(100),
  CONSTRAINT "PK_careers" PRIMARY KEY (career_id),
  CONSTRAINT "FK_careers_institutions" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
);

CREATE TABLE IF NOT EXISTS Users
(
  user_id uuid NOT NULL DEFAULT uuid(),
  username character varying(100),
  email character varying(320) NOT NULL,
  password character varying(100),
  first_name character varying(100),
  last_name character varying(100),
  institution_id uuid,
  biography text,
  CONSTRAINT "PK_users" PRIMARY KEY (user_id),
  CONSTRAINT "UQ_users_email" UNIQUE (email),
  CONSTRAINT "UQ_users_username" UNIQUE (username),
  CONSTRAINT "FK_users_institutions" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
);

CREATE TABLE IF NOT EXISTS Directories
(
  directory_id uuid NOT NULL DEFAULT uuid(),
  directory_name character varying(100),
  parent_id uuid,
  user_id uuid,
  CONSTRAINT "PK_directories" PRIMARY KEY (directory_id),
  CONSTRAINT "UQ_directories" UNIQUE (directory_name, user_id, parent_id),
  CONSTRAINT "FK_directories_users" FOREIGN KEY (user_id) REFERENCES Users (user_id),
  CONSTRAINT "FK_directories_directories" FOREIGN KEY (parent_id) REFERENCES Directories (directory_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Subjects
(
  subject_id uuid NOT NULL DEFAULT uuid(),
  subject_name character varying(100),
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
  note_id uuid NOT NULL DEFAULT uuid(),
  note_name varchar(100) NOT NULL,
  user_id uuid, -- TODO: Check if it should be NOT NULL
  file bytea,
  category varchar(100) CHECK (category IN ('practice', 'theory', 'exam', 'other')),
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
  CONSTRAINT "PK_reviews" PRIMARY KEY (note_id, user_id),
  CONSTRAINT "FK_reviews_notes" FOREIGN KEY (note_id) REFERENCES Notes (note_id) ON DELETE CASCADE,
  CONSTRAINT "FK_reviews_users" FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);