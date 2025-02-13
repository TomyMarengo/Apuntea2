INSERT INTO Institutions (institution_id, institution_name) VALUES ('10000000-0000-0000-0000-000000000000', 'ITBA');
INSERT INTO Institutions (institution_id, institution_name) VALUES ('10000000-0000-0000-0000-000000000001', 'UTN');

INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000000', 'Ingenieria Informatica', '10000000-0000-0000-0000-000000000000');
INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000001', 'Ingenieria Mecanica', '10000000-0000-0000-0000-000000000000');
INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000002', 'Ingenieria en Sistemas', '10000000-0000-0000-0000-000000000001');

INSERT INTO Users (user_id, email, career_id) VALUES ('00000000-0000-0000-0000-000000000000','pepe@itba.edu.ar', 'c0000000-0000-0000-0000-000000000000');
INSERT INTO Users (user_id, email, career_id) VALUES ('00000000-0000-0000-0000-000000000001','jaimito@itba.edu.ar', 'c0000000-0000-0000-0000-000000000000');
INSERT INTO Users (user_id, email, career_id) VALUES ('00000000-0000-0000-0000-000000000002','carla@itba.edu.ar', 'c0000000-0000-0000-0000-000000000000');
INSERT INTO Users (user_id, email, career_id) VALUES ('00000000-0000-0000-0000-000000000003','saidman@utn.edu.ar', 'c0000000-0000-0000-0000-000000000002');
INSERT INTO Users (user_id, email, career_id, status) VALUES ('00000000-0000-0000-0000-000000000004','profano@utn.edu.ar', 'c0000000-0000-0000-0000-000000000002', 'BANNED');

INSERT INTO User_Roles (user_id, role_name) VALUES ('00000000-0000-0000-0000-000000000000', 'ROLE_STUDENT');
INSERT INTO User_Roles (user_id, role_name) VALUES ('00000000-0000-0000-0000-000000000001', 'ROLE_STUDENT');
INSERT INTO User_Roles (user_id, role_name) VALUES ('00000000-0000-0000-0000-000000000002', 'ROLE_ADMIN');
INSERT INTO User_Roles (user_id, role_name) VALUES ('00000000-0000-0000-0000-000000000003', 'ROLE_STUDENT');
INSERT INTO User_Roles (user_id, role_name) VALUES ('00000000-0000-0000-0000-000000000004', 'ROLE_STUDENT');

INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000000', 'EDA');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000001', 'PAW');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000002', 'Mecanica Gral');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000003', 'Dinamica de Fluidos');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000004', 'Java Beans 101');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-00000000000b', 'Matematica I');

INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-000000000005', 'Guias', 'd0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000');
INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-000000000006', '1eros parciales', 'd0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000');

INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-000000000007', 'Teoria', 'd0000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000');
INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-000000000008', 'MVC', 'd0000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000000');

INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-000000000009', 'Basura', 'd0000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000');
INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-00000000000a', 'Basura 2', 'd0000000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000000');

INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-00000000000c', '12 % 3 = 0', 'd0000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000000');
INSERT INTO Directories (directory_id, directory_name, parent_id, user_id) VALUES ('d0000000-0000-0000-0000-00000000000d', 'Gr_y', 'd0000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000000');

INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000000', 'EDA', 'd0000000-0000-0000-0000-000000000000');
INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000001', 'PAW', 'd0000000-0000-0000-0000-000000000001');
INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000002', 'Mecanica Gral', 'd0000000-0000-0000-0000-000000000002');
INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000003', 'Dinamica de Fluidos', 'd0000000-0000-0000-0000-000000000003');
INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000004', 'Java Beans 101', 'd0000000-0000-0000-0000-000000000004');
INSERT INTO Subjects (subject_id, subject_name, root_directory_id) VALUES ('50000000-0000-0000-0000-000000000005', 'Matematica I', 'd0000000-0000-0000-0000-00000000000b');

INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000000', 'c0000000-0000-0000-0000-000000000000'); -- EDA - Informatica
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000000'); -- PAW - Informatica
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000001'); -- Mecanica Gral - Mecanica
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000003', 'c0000000-0000-0000-0000-000000000001'); -- Dinamica de Fluidos - Mecanica
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000004', 'c0000000-0000-0000-0000-000000000002'); -- Java Beans - Sistemas
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000000'); -- Mate I - Informatica
INSERT INTO Subjects_Careers (subject_id, career_id) VALUES ('50000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000001'); -- Mate I - Mecanica

INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000000', 'guia 1 de eda', '00000000-0000-0000-0000-000000000000', 'PRACTICE', '50000000-0000-0000-0000-000000000000', 'd0000000-0000-0000-0000-000000000000', 'pdf', '2022-09-01 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000001', 'Lucene - teoria', '00000000-0000-0000-0000-000000000000', 'THEORY', '50000000-0000-0000-0000-000000000000', 'd0000000-0000-0000-0000-000000000000', 'pdf', '2022-09-02 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000002', 'MVC vs API REST', '00000000-0000-0000-0000-000000000000', 'THEORY', '50000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000001', 'pdf', '2022-09-03 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000003', 'Guia 1 - Cinematica', '00000000-0000-0000-0000-000000000000', 'PRACTICE', '50000000-0000-0000-0000-000000000002', 'd0000000-0000-0000-0000-000000000002', 'pdf', '2022-09-04 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000004', 'Parcial 2 2018 - Dinamica de Fluidos', '00000000-0000-0000-0000-000000000000', 'EXAM', '50000000-0000-0000-0000-000000000003', 'd0000000-0000-0000-0000-000000000003', 'pdf', '2022-09-05 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000005', 'Java Beans Everywhere', '00000000-0000-0000-0000-000000000000', 'THEORY', '50000000-0000-0000-0000-000000000004', 'd0000000-0000-0000-0000-000000000004', 'pdf', '2022-09-06 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000006', 'Teorema del Valor Medio', '00000000-0000-0000-0000-000000000000', 'THEORY', '50000000-0000-0000-0000-000000000005', 'd0000000-0000-0000-0000-00000000000b', 'pdf', '2022-09-07 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at) VALUES ('a0000000-0000-0000-0000-000000000007', 'Java Beans pero de PAW', '00000000-0000-0000-0000-000000000000', 'PRACTICE', '50000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000007', 'pdf', '2022-09-08 19:24:53.307060');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type, created_at, visible) VALUES ('a0000000-0000-0000-0000-000000000008', 'El secreto para aprobar PAW', '00000000-0000-0000-0000-000000000001', 'EXAM', '50000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000001', 'pdf', '2022-09-09 19:24:53.307060', false);

INSERT INTO Reviews (note_id, user_id, score, created_at) VALUES ('a0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000001', 3, '2023-09-01 19:24:53.307060');
INSERT INTO Reviews (note_id, user_id, score, created_at) VALUES ('a0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 4, '2023-09-02 19:25:53.307060');
INSERT INTO Reviews (note_id, user_id, score, created_at) VALUES ('a0000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000000', 5, '2023-09-03 19:26:53.307060');
INSERT INTO Reviews (note_id, user_id, score, created_at) VALUES ('a0000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 1, '2023-09-04 19:27:53.307060');