# 🏥 MedAppoint - Technical Project Report

## 1. Project Overview
**MedAppoint** is a complete medical appointment management system designed to facilitate interaction between patients, doctors, and administrative staff. The solution consists of a **Spring Boot Backend** API and a native **Android Mobile Application**.

A key engineering feature of MedAppoint is its **Resilient Dual-Mode Architecture**, allowing the application to function seamlessly in both "Online" (Cloud-connected) and "Offline" (Standalone Demo) environments.

| **Project Metadata** | Details |
| :--- | :--- |
| **Project Name** | MedAppoint |
| **Team Members** | Amine, Zouhair, Nouha, Rabab |
| **Primary Language** | Java (Backend & Mobile) |
| **Architecture** | Client-Server (REST API) |

## 2. Technical Stack

### 🟢 Backend (Server)
*   **Framework:** **Spring Boot** (Java) - robust and scalable REST API.
*   **Database:** **PostgreSQL** - relational database for persistent storage.
*   **ORM:** **Spring Data JPA (Hibernate)** - automatic object-relational mapping.
*   **Security:** Custom Role-Based Access Control (RBAC) implementation.
*   **Deployment:** Dockerized for cloud deployment (Koyeb/Serveo tunneling).

### 🔵 Frontend (Mobile Client)
*   **Platform:** **Android** (Native Java).
*   **Networking:** **Retrofit 2** - for type-safe HTTP client requests.
*   **UI/UX:** XML Layouts with Material Design components (CardViews, FloatingActionButtons).
*   **Compatibility:** Minimum SDK verified for broad device support.

## 3. Architecture & Key Modules

### A. The "Dual-Mode" Connectivity Strategy
The application implements a unique `RetrofitClient` singleton that can toggle between two modes:
1.  **Online Mode:** Connects to the live Spring Boot backend via HTTPS. Includes a custom `UnsafeOkHttpClient` to handle SSL complexities during development tunneling (Serveo).
2.  **Offline Demo Mode:** Uses a `MockApiService` and in-memory `MockData`. This simulates the *exact* behavior of the backend (User auth, CRUD operations) locally on the phone. This ensures **0% failure rate** during presentations, even without internet.

### B. Backend Structure
The backend is organized into a clean MVC-style Layered Architecture:
*   **Models:** `User.java` (handles authentication & roles), `Appointment.java`.
*   **Controllers:**
    *   `AuthController`: Handles `/login` and `/register`.
    *   `AdminController`: Allows admins to view pending appointments and assign doctors.
    *   `PatientController`: Manages patient booking flows.
    *   `DoctorController`: Manages doctor schedules.

### C. Database Schema (PostgreSQL/Import.sql)
The system is pre-configured with the following entities:
*   **Users Table:** Stores credentials and roles (`ADMIN`, `DOCTOR`, `PATIENT`).
*   **Appointments Table:** Links `patient_id` and `doctor_id` with status tracking (`PENDING`, `CONFIRMED`, `COMPLETED`).
*   **Initial Data:** The system auto-initializes with 1 Admin, 1 Doctor, and 2 Patients for immediate testing.

## 4. Functional Scope by Role

### 👤 Patient Role
*   **Dashboard:** View personal appointment history.
*   **Booking:** Book new appointments (Date, Time, Reason).
*   **Status Tracking:** See if their request is `PENDING` or `CONFIRMED`.

### 👨‍⚕️ Doctor Role
*   **Dashboard:** View list of verified/assigned appointments.
*   **Management:** Mark appointments as `COMPLETED`.

### 🛡️ Admin Role (Secretariat)
*   **Triage:** View all `PENDING` appointment requests.
*   **Assignment:** Assign a specific doctor to a request (updates status to `CONFIRMED`).
*   **User Management:** Ability to view/delete users.

## 5. Current Implementation Status
*   **User Interface:** Fully implemented with distinct Dashboards for each role (`PatientDashboardActivity`, `AdminDashboardActivity`, `DoctorDashboardActivity`).
*   **Authentication:** Functional Login/Register flow with role redirection.
*   **Data Consistency:** The `import.sql` script ensures a consistent dataset relies on every server restart.
*   **Stability:** The decision to include an Offline Mode mitigates all risks associated with university Wi-Fi or firewall restrictions (`client isolation`).

### 📝 Final Verification Note
The project is structurally sound. The separation of concerns (Backend API vs. Android Client) is well maintained. The inclusion of the **Offline Demo Mode** allows for a flawless demonstration, effectively decoupling the presentation success from network reliability.
