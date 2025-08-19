# 🏥 Hospital and OPD Management System

A robust and scalable **Hospital and Outpatient Department (OPD) Management System** designed to optimize and streamline
hospital operations. This system facilitates efficient handling of patient appointments, doctor schedules, billing,
medical histories, and more, tailored for hospitals of all sizes.

Live URL: https://ayumed.netlify.app/

## ✨ Key Features

### 🧑‍⚕️ Patient & Doctor Management

- Comprehensive patient records & profiles 📋
- Doctor & staff schedules, specialties, and departments 🩺
- Appointment booking with reminders 📅

### 💳 Billing & Payments

- Automated billing & invoice generation 💳
- Track payment status & history 📊

### 🏥 Hospital Services

- Search hospitals nearby 🌍
- Pharmacy management with real-time drug tracking ⚕️
- Bulk data import (doctors, patients, medications) via Excel 📑

### 📂 Records & Storage

- Secure medical history & test results 🧬 stored in **Cloudinary**
- Patient reports & documents 📃 with encrypted Cloudinary storage

### 📧 Notifications & Feedback

- OTP verification for login & password reset 🔑
- Welcome & reminder emails 📩
- Patient feedback mechanism ⭐

## 🎥 Demo

<!-- Banner at the top -->
<b>Hospital Management System Banner</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755622126/Screenshot_2025-08-19_220306_vhjkn9.png" height="300px" />


---

<!-- Login and Dashboard side by side -->
<table>
<tr>
<td>
<b>Login / Registration with OTP</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755622275/Screenshot_2025-08-19_222101_uwod4g.png" height="400px" />
</td>
<td>
<b>Dashboard showing appointments & statistics</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755622445/FireShot_Capture_009_-_Vite_React_-_ayumed.netlify.app_m2naee.png" height="400px" />
</td>
</tr>
</table>

---

<!-- Appointment Booking and Patient Records -->
<table>
<tr>
<td>
<b>Patient booking an appointment</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755623355/FireShot_Capture_010_-_AyuMed_-_ayumed.netlify.app_hltw0b.png" height="400px" />
</td>
<td>
<b>Upcoming appointments & history</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755623412/FireShot_Capture_011_-_AyuMed_-_ayumed.netlify.app_vrocs3.png" height="400px" />
</td>
</tr>
</table>

---

<!-- Pharmacy and Medical History -->
<table>
<tr>
<td>
<b>Medication & pharmacy management</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755623541/FireShot_Capture_012_-_AyuMed_-_ayumed.netlify.app_qrsmp8.png" height="400px" />
</td>
<td>
<b>Medical history & test reports</b>
<hr>
<img src="https://res.cloudinary.com/dft2nlkmz/image/upload/v1755623622/FireShot_Capture_014_-_AyuMed_-_ayumed.netlify.app_qryerc.png" height="400px" />
</td>
</tr>
</table>

## Tech Stack 🛠️

- **Backend**: Java, Spring Boot, Spring Data JPA, Hibernate
- **Database**: MySQL or Postgres (configurable with other SQL databases)
- **File Handling**: Apache POI (for Excel data import)
- **Frontend**: HTML, CSS, JavaScript, React.js, MUI, Antd etc.
- **Security**: Spring Security with JWT authentication, role-based access control
- **Email Service**:  SMTP configuration via Brevo (Sendinblue) for automated notifications
- **Testing**: JUnit and Mockito for unit and integration testing
- **API Testing**: Postman is used for API Testing

## Project Structure 📂

```
.
├── Backend - SpringBoot
│   ├── src
│   │   ├── controller
│   │   ├── exception
│   │   ├── configuration
│   │   ├── entity
│   │   ├── dto
│   │   ├── service
│   │   ├── repository
│   ├── resources
├── Frontend - React.js
│   ├── src
│   │   ├── Api & Services
│   │   ├── auth
│   │   ├── Components
│   │   ├── Pages
│   │   ├── Profile
│   │   ├── Styles
│   │   ├── Users
│   │   │   ├── Doctor
│   │   │   ├── Manager
│   │   │   ├── Patient
│   │   │   ├── Pharmacist
│   ├── public
├── docker-compose.yml
```

## 🏗️ System Architecture

```mermaid
flowchart TD

    %% Users
    User((Patient / Doctor / Pharmacist / Manager))

    %% Frontend
    User -->|Browser Access| Frontend[React.js Frontend]
    Frontend -->|REST/HTTPS| Backend[Spring Boot Backend]

    %% Core Backend
    Backend --> DB[(Postgres / MySQL Database)]
    Backend --> Cloudinary[(Cloudinary - File Storage)]
    Backend --> Email[SMTP (Brevo) - Email/OTP Service]

    %% Notes
    classDef infra fill=#fdf6e3,stroke=#657b83,stroke-width=2px;
    classDef service fill=#e6f7ff,stroke=#1890ff,stroke-width=2px;
    classDef frontend fill=#f0fff4,stroke=#52c41a,stroke-width=2px;
    classDef user fill=#fff5f5,stroke=#ff4d4f,stroke-width=2px;

    class User user
    class Frontend frontend
    class Backend service
    class DB,Cloudinary,Email infra
```

## Configuration Files 🔧

### `application.properties`

Define key settings for the database, server, and file handling in `src/main/resources/application.properties`:

```properties
# Server Port
server.port=8080
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
cloudinary.cloud_name=${CLOUD_NAME}
cloudinary.api_key=${CLOUDINARY_API_KEY}
cloudinary.api_secret=${CLOUDINARY_API_SECRET}

```

## Getting Started 🚀

To get your application up and running, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/hospital-opd-management.git
   ```

2. **Set up the Database**:
    - Create a MySQL database named `hospital_db`. You can do this using a MySQL client or command line:
    ```sql
    CREATE DATABASE hospital_db;
    ```
    - Update your database credentials in `src/main/resources/application.properties`. Below is an example of the
      database configuration:

   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # Hibernate Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

3. **Run the Application**:
    - To start the spring-application, you can use your Integrated Development Environment (IDE) or run the following
      command in your terminal:
       ```bash
       ./mvnw spring-boot:run
       ```

    - Make sure you have the correct version of Java installed (Java 23 or above is recommended). You can verify your
      Java version by running:
       ```bash
       java -version
      ```

    - To start the react-application, you can use your Integrated Development Environment (IDE) or run the following
      command in your terminal:
        ```bash
        npm run dev
        ```

    - Make sure you have the correct version of Node.js installed. You can verify your Node.js version by running:
       ```bash
       npm --version
      ```

4. **Access the API Documentation**:
    - If you are using Springdoc OpenAPI or Swagger for API documentation, you can access it by navigating to the
      following URLs in your web browser:
        - [Swagger UI](http://localhost:8080/swagger-ui.html) - A user-friendly interface to test the API endpoints.
        - [Swagger API Documentation](http://localhost:8080/v3/api-docs) - View the API documentation in JSON format.

## Sample Excel File for Doctor Registration

To bulk register doctors in the system, use the following Excel format:

[Download the sample file here](https://iiitranchiacin-my.sharepoint.com/:x:/g/personal/dhruv_2022ug2022_iiitranchi_ac_in/Ee2ReM-OWBFAjgF3XYg3k1kB6GZT62M4n02lzetVwwPtIw?e=o15MJd)

| Username  | Password    | First Name | Last Name | Gender | Email                       | Mobile     | Department  | Specialty         | License Number |
|-----------|-------------|------------|-----------|--------|-----------------------------|------------|-------------|-------------------|----------------|
| ram123    | password123 | Ram        | Sharma    | MALE   | ram.sharma@example.com      | 9876543210 | Cardiology  | Heart Surgery     | ABC12345       |
| shiv456   | password456 | Shiva      | Pandey    | MALE   | shivam456.smith@example.com | 9876543211 | Neurology   | Brain Surgery     | DEF67890       |
| sam789    | password789 | Samira     | Singh     | FEMALE | sameera@example.com         | 9876543212 | Orthopedics | Joint Replacement | GHI11223       |
| pankaj101 | password101 | Pankaj     | Dubey     | MALE   | dubey.pankaj@example.com    | 9876543213 | Pediatrics  | Pediatric Surgery | JKL33445       |

## Sample Excel File for Medications Import

To bulk import medications in the pharmacy, use the following Excel format:

[Download the sample file here](https://iiitranchiacin-my.sharepoint.com/:x:/g/personal/dhruv_2022ug2022_iiitranchi_ac_in/EYKmFAVggipArM6yTPmDwNUBSmxHATk713LIGBbe0bO9tw?e=YAHCxa)

| Medication Name | Composition Name         | Dosage Form | Strength | Quantity | Expiry Date | Manufacturer | Price  | Batch Number |
|-----------------|--------------------------|-------------|----------|----------|-------------|--------------|--------|--------------|
| Paracetamol     | Acetaminophen            | Tablet      | 500mg    | 100      | 2025-07-02  | PharmaCorp   | ₹10.00 | BATCH6043    |
| Ibuprofen       | Ibuprofen                | Tablet      | 200mg    | 50       | 2025-04-21  | HealthMed    | ₹5.00  | BATCH7089    |
| Amoxicillin     | Amoxicillin Trihydrate   | Capsule     | 250mg    | 75       | 2025-06-01  | GlobalPharm  | ₹15.00 | BATCH8034    |
| Cetirizine      | Cetirizine Hydrochloride | Tablet      | 10mg     | 30       | 2024-12-21  | WellnessLabs | ₹7.00  | BATCH2687    |
| Metformin       | Metformin Hydrochloride  | Tablet      | 500mg    | 120      | 2025-06-08  | LifeCare     | ₹20.00 | BATCH5549    |
| Atorvastatin    | Atorvastatin Calcium     | Tablet      | 10mg     | 60       | 2025-03-29  | CholSafe     | ₹25.00 | BATCH7330    |
| Omeprazole      | Omeprazole Magnesium     | Capsule     | 20mg     | 40       | 2025-06-18  | AcidGuard    | ₹30.00 | BATCH4494    |
| Amlodipine      | Amlodipine Besylate      | Tablet      | 5mg      | 90       | 2025-03-23  | BloodFlow    | ₹18.00 | BATCH1334    |
| Simvastatin     | Simvastatin              | Tablet      | 20mg     | 80       | 2025-07-28  | HeartHealth  | ₹22.00 | BATCH7100    |
| Aspirin         | Acetylsalicylic Acid     | Tablet      | 75mg     | 150      | 2025-05-05  | PainAway     | ₹8.00  | BATCH1424    |

### How to Use:

1. Download the sample file.
2. Fill in the medications information as per the required format.
3. Upload the Excel file to the system to add the medications.

## Current Status

🚧 **Project Status**: _In Progress_

The core features of the system are actively being developed and tested. Expect ongoing updates as new features are
added and existing ones are refined. Please check back for future updates as we move towards a complete version.

## Future Plans

Once completed, the Hospital Management System will offer:

- **Comprehensive Dashboard** for hospital administrators and managers
- **Detailed Analytics** for data-driven insights
- **Patient Portal** for viewing appointments and medical history
- **Integration with Third-Party Systems** for expanded functionality
- **Aadhaar Authentication** for added security and details fetching
- **Medical Records** making a centralized platform for storing all the records in an encrypted way for easy
  accessibility.

## 🤝 Contribution Guidelines

We welcome contributions! 🎉

- 🐛 **Bug Reports** → Open a GitHub Issue
- 💡 **Feature Requests** → Use the Discussions tab
- 🛠️ **Code Contributions** → Fork & create a PR

If you'd like to contribute, please follow these steps:

1. **Fork the Repository**: Click the "Fork" button at the top right of the repository page.
2. **Create a Feature Branch**: Use the command:
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Commit Your Changes**: Make your changes and commit them with clear messages.
   ```bash
   git commit -m "Add some feature"
   ```

4. **Push to the Branch**: Push your changes to your forked repository.
    ```bash
   git push origin feature/your-feature-name
    ```

5. **Open a Pull Request**: Go to the original repository and click "New Pull Request".

Your contributions will help enhance functionality, add new features, improve code quality, and fix bugs. Thank you for
considering contributing!

⭐ If you like this project, don’t forget to **star** the repo!

## 📄 License

This project is licensed under the [MIT License](LICENSE.txt) .

## 💬 Contact

For questions or suggestions, feel free to reach out:

- **Email:** dhruvgupta130@gmail.com
- **LinkedIn:** [Dhruv Gupta](https://www.linkedin.com/in/dhruvgupta130)

---

🚀 Built with passion by Dhruv Gupta

