-- Création des utilisateurs de test
-- On utilise ON CONFLICT DO NOTHING pour éviter les erreurs si on relance l'application (Note: ça dépend de la DB, mais ici on fait simple INSERT)

-- 1. Un Administrateur
INSERT INTO users (id, full_name, email, password, role) VALUES (1, 'System Admin', 'admin@test.com', 'admin123', 'ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO users (id, full_name, email, password, role) VALUES (2, 'Dr. Ahmed Benali', 'doctor@test.com', 'doctor123', 'DOCTOR') ON CONFLICT (id) DO NOTHING;
INSERT INTO users (id, full_name, email, password, role) VALUES (3, 'Zouhair El Moutaouakil', 'patient@test.com', 'patient123', 'PATIENT') ON CONFLICT (id) DO NOTHING;

INSERT INTO appointments (id, date, time, reason, patient_name, patient_id, doctor_id, doctor_name, status) VALUES (1, '2024-12-30', '09:00', 'Consultation Générale', 'Zouhair El Moutaouakil', 3, 2, 'Dr. Ahmed Benali', 'CONFIRMED') ON CONFLICT (id) DO NOTHING;
INSERT INTO appointments (id, date, time, reason, patient_name, patient_id, doctor_id, doctor_name, status) VALUES (2, '2025-01-05', '14:30', 'Suivi Cardiologie', 'Zouhair El Moutaouakil', 3, 2, 'Dr. Ahmed Benali', 'PENDING') ON CONFLICT (id) DO NOTHING;

-- Mise à jour des séquences pour éviter les erreurs d'ID auto-générés après insertion manuelle
-- (Ceci est spécifique à PostgreSQL)
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('appointments_id_seq', (SELECT MAX(id) FROM appointments));
