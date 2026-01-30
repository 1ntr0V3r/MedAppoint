-- Création des utilisateurs de test
-- On utilise ON CONFLICT DO NOTHING pour éviter les erreurs

-- 1. Un Administrateur
INSERT INTO users (id, full_name, email, password, role) VALUES (1, 'System Admin', 'admin@test.com', 'admin123', 'ADMIN') ON CONFLICT (id) DO NOTHING;

-- 2. Un Docteur
INSERT INTO users (id, full_name, email, password, role) VALUES (2, 'Dr. Ahmed Benali', 'doctor@test.com', 'doctor123', 'DOCTOR') ON CONFLICT (id) DO NOTHING;

-- 3. Un Patient
INSERT INTO users (id, full_name, email, password, role) VALUES (3, 'Zouhair El Moutaouakil', 'patient@test.com', 'patient123', 'PATIENT') ON CONFLICT (id) DO NOTHING;

-- 4. Un User de Test
INSERT INTO users (id, full_name, email, password, role) VALUES (10, 'Test Power', 'power@test.com', 'password', 'PATIENT') ON CONFLICT (id) DO NOTHING;

-- Rendez-vous
INSERT INTO appointments (id, date, time, reason, patient_name, patient_id, doctor_id, doctor_name, status) VALUES (1, '2024-12-30', '09:00', 'Consultation Générale', 'Zouhair El Moutaouakil', 3, 2, 'Dr. Ahmed Benali', 'CONFIRMED') ON CONFLICT (id) DO NOTHING;

-- Mise à jour des séquences (Important pour PostgreSQL)
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('appointments_id_seq', (SELECT MAX(id) FROM appointments));
