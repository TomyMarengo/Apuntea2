INSERT INTO Institutions (institution_id, institution_name) VALUES ('10000000-0000-0000-0000-000000000000', 'ITBA');
INSERT INTO Institutions (institution_id, institution_name) VALUES ('10000000-0000-0000-0000-000000000001', 'UTN');

INSERT INTO Users (user_id, email) VALUES ('00000000-0000-0000-0000-000000000000','pepe@itba.edu.ar');
INSERT INTO Users (user_id, email) VALUES ('00000000-0000-0000-0000-000000000001','jaimito@itba.edu.ar');
INSERT INTO Users (user_id, email) VALUES ('00000000-0000-0000-0000-000000000002','carla@itba.edu.ar');
INSERT INTO Users (user_id, email) VALUES ('00000000-0000-0000-0000-000000000003','saidman@utn.edu.ar');

INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000000', 'EDA');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000001', 'PAW');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000002', 'Mecanica Gral');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000003', 'Dinamica de Fluidos');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-000000000004', 'Java Beans 101');
INSERT INTO Directories (directory_id, directory_name) VALUES ('d0000000-0000-0000-0000-00000000000b', 'Matematica I');

INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-000000000005', 'Guias', 'd0000000-0000-0000-0000-000000000000');
INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-000000000006', '1eros parciales', 'd0000000-0000-0000-0000-000000000000');

INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-000000000007', 'Teoria', 'd0000000-0000-0000-0000-000000000001');
INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-000000000008', 'MVC', 'd0000000-0000-0000-0000-000000000007');

INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-000000000009', 'Basura', 'd0000000-0000-0000-0000-000000000001');
INSERT INTO Directories (directory_id, directory_name, parent_id) VALUES ('d0000000-0000-0000-0000-00000000000a', 'Basura 2', 'd0000000-0000-0000-0000-000000000009');

INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000000', 'Ingenieria Informatica', '10000000-0000-0000-0000-000000000000');
INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000001', 'Ingenieria Mecanica', '10000000-0000-0000-0000-000000000000');
INSERT INTO Careers (career_id, career_name, institution_id) VALUES ('c0000000-0000-0000-0000-000000000002', 'Ingenieria en Sistemas', '10000000-0000-0000-0000-000000000001');

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

INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000000', 'guia 1 de eda', '00000000-0000-0000-0000-000000000000', 'practice', '50000000-0000-0000-0000-000000000000', 'd0000000-0000-0000-0000-000000000000', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000001', 'Lucene - teoria', '00000000-0000-0000-0000-000000000000', 'theory', '50000000-0000-0000-0000-000000000000', 'd0000000-0000-0000-0000-000000000000', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000002', 'MVC vs API REST', '00000000-0000-0000-0000-000000000000', 'theory', '50000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000001', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000003', 'Guia 1 - Cinematica', '00000000-0000-0000-0000-000000000000', 'practice', '50000000-0000-0000-0000-000000000002', 'd0000000-0000-0000-0000-000000000002', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000004', 'Parcial 2 2018 - Dinamica de Fluidos', '00000000-0000-0000-0000-000000000000', 'exam', '50000000-0000-0000-0000-000000000003', 'd0000000-0000-0000-0000-000000000003', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000005', 'Java Beans Everywhere', '00000000-0000-0000-0000-000000000000', 'theory', '50000000-0000-0000-0000-000000000004', 'd0000000-0000-0000-0000-000000000004', 'pdf');
INSERT INTO Notes (note_id, note_name, user_id, category, subject_id, parent_id, file_type) VALUES ('a0000000-0000-0000-0000-000000000006', 'Teorema del Valor Medio', '00000000-0000-0000-0000-000000000000', 'theory', '50000000-0000-0000-0000-000000000005', 'd0000000-0000-0000-0000-00000000000b', 'pdf');

INSERT INTO Reviews (note_id, user_id, score) VALUES ('a0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000001', 3);
INSERT INTO Reviews (note_id, user_id, score) VALUES ('a0000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 4);
INSERT INTO Reviews (note_id, user_id, score) VALUES ('a0000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000000', 5);
INSERT INTO Reviews (note_id, user_id, score) VALUES ('a0000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 1);
