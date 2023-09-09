--CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS Institutions
(
    institution_id uuid NOT NULL DEFAULT UUID(),
    name varchar(100),
    CONSTRAINT "Institutions_pkey" PRIMARY KEY (institution_id)
);

CREATE TABLE IF NOT EXISTS Careers
(
    career_id uuid NOT NULL DEFAULT UUID(),
    institution_id uuid NOT NULL,
    name character varying(30),
    CONSTRAINT "Careers_pkey" PRIMARY KEY (career_id),
    CONSTRAINT "FK_career_institution" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
);

CREATE TABLE IF NOT EXISTS Users
(
    user_id uuid NOT NULL DEFAULT UUID(),
    username character varying(30),
    email character varying(320) NOT NULL,
    password character varying(30),
    name character varying(30),
    surname character varying(30),
    institution_id uuid,
    biography text ,
    CONSTRAINT "Users_pkey" PRIMARY KEY (user_id),
    CONSTRAINT "UQ_email" UNIQUE (email),
    CONSTRAINT "UQ_username" UNIQUE (username),
    CONSTRAINT "FK_user_institution" FOREIGN KEY (institution_id) REFERENCES Institutions (institution_id)
    );

CREATE TABLE IF NOT EXISTS Directories
(
    directory_id uuid NOT NULL DEFAULT UUID(),
    name character varying(30),
    parent_id uuid,
    user_id uuid,
    CONSTRAINT "Directories_pkey" PRIMARY KEY (directory_id),
    CONSTRAINT "FK_directory_user" FOREIGN KEY (user_id) REFERENCES Users (user_id)
    );

CREATE TABLE IF NOT EXISTS Subjects
(
    subject_id uuid NOT NULL DEFAULT UUID(),
    name character varying(30),
    root_directory_id uuid,
    career_id uuid,
    semester smallint,
    CONSTRAINT "Subjects_pkey" PRIMARY KEY (subject_id),
    CONSTRAINT "FK_subject_directory" FOREIGN KEY (root_directory_id) REFERENCES Directories (directory_id),
    CONSTRAINT "FK_subject_career" FOREIGN KEY (career_id) REFERENCES Careers (career_id)
);

CREATE TABLE IF NOT EXISTS Notes
(
    note_id uuid NOT NULL DEFAULT UUID(),
    name varchar(30) NOT NULL,
    user_id uuid, -- TODO: Check if it should be NOT NULL
    file bytea NOT NULL,
    category varchar(30),
    subject_id uuid,
    parent_directory_id uuid,
    created_at timestamp DEFAULT now(),
    CONSTRAINT "Notes_pkey" PRIMARY KEY (note_id),
    CONSTRAINT "FK_note_directory" FOREIGN KEY (parent_directory_id) REFERENCES Directories (directory_id),
    CONSTRAINT "FK_note_subject" FOREIGN KEY (subject_id) REFERENCES Subjects (subject_id),
    CONSTRAINT "FK_note_user" FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

CREATE TABLE IF NOT EXISTS Reviews
(
    note_id uuid NOT NULL,
    user_id uuid NOT NULL,
    score smallint NOT NULL,
    CONSTRAINT "Reviews_pkey" PRIMARY KEY (note_id, user_id),
    CONSTRAINT "FK_review_note" FOREIGN KEY (note_id) REFERENCES Notes (note_id),
    CONSTRAINT "FK_review_user" FOREIGN KEY (user_id) REFERENCES Users (user_id)
)