# 🏥 Hospital and OPD Management System

A robust and scalable **Hospital and Outpatient Department (OPD) Management System** designed to optimize and streamline hospital operations. This system facilitates efficient handling of patient appointments, doctor schedules, billing, medical histories, and more, tailored for hospitals of all sizes.

## Key Features 🚀

- **Comprehensive Patient Management**: Register, update, and maintain patient records 📋
- **Doctor and Staff Administration**: Efficiently manage doctor schedules, departments, and specialties 🩺
- **Appointment Scheduling**: Book and track patient appointments for seamless interaction 📅
- **Billing and Payments**: Manage billing, track payments, and generate invoices 💳
- **Search Hospital Nearby**: Get Hospitals nearby your location by selecting the radius 🌍
- **Medical History and Test Records**: Securely store patient medical histories and test results 🧬
- **Patient Records**: A dedicated storage for Patient's reports and documents 📃
- **Feedback Mechanism**: Gather and analyze patient feedback to improve service quality ⭐
- **Excel Import**: Import bulk doctor or patient data from Excel files using Apache POI 📊

## Tech Stack 🛠️

- **Backend**: Java, Spring Boot, Spring Data JPA, Hibernate
- **Database**: MySQL (configurable with other SQL databases)
- **File Handling**: Apache POI (for Excel data import)
- **Frontend**: HTML, CSS, JavaScript (or any preferred frontend framework)
- **Security**: Spring Security (recommended for production)
- **Testing**: JUnit and Mockito for unit and integration testing

## Project Structure 📂

- `src/main/java`: Contains core Java classes, including entities, services, controllers, and repositories
   - `entities/`: Java classes representing the database tables, such as Patient, Doctor, Appointment, and Billing
   - `services/`: Business logic and service layer classes
   - `controllers/`: REST API endpoints for handling requests
   - `repositories/`: Data access layer for interacting with the database
- `src/main/resources`: Stores application configuration files
   - `application.properties`: Configures database connections, server ports, and other settings
   - `data.sql`: Optional file for seeding initial data (patients, doctors, etc.) into the database
   - `schema.sql`: Optional file for defining database schemas, if not using JPA auto-configuration
- `src/test`: Contains test cases to ensure code reliability and quality


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

file.storage.path= storage/patients

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
   - Update your database credentials in `src/main/resources/application.properties`. Below is an example of the database configuration:

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
   - To start the application, you can use your Integrated Development Environment (IDE) or run the following command in your terminal:
   ```bash
   ./mvnw spring-boot:run
   ```

   - Make sure you have the correct version of Java installed (Java 11 or above is recommended). You can verify your Java version by running:
      ```bash
      java -version
     ```

4. **Access the API Documentation**:
   - If you are using Springdoc OpenAPI or Swagger for API documentation, you can access it by navigating to the following URLs in your web browser:
      - [Swagger UI](http://localhost:8080/swagger-ui.html) - A user-friendly interface to test the API endpoints.
      - [Swagger API Documentation](http://localhost:8080/v3/api-docs) - View the API documentation in JSON format.

## Sample Excel File for Doctor Registration

To bulk register doctors in the system, use the following Excel format:

[Download the sample file here](https://iiitranchiacin-my.sharepoint.com/:x:/g/personal/dhruv_2022ug2022_iiitranchi_ac_in/Ee2ReM-OWBFAjgF3XYg3k1kB6GZT62M4n02lzetVwwPtIw?e=o15MJd)

| Username  | Password    | Gender | First Name | Last Name | Email                       | Mobile     | Department     |  Specialty        | License Number |
|-----------|-------------|--------|------------|-----------|-----------------------------|------------|----------------|-------------------|----------------|
| ram123    | password123 | MALE   | Ram        | Sharma    | ram.sharma@example.com      | 9876543210 | Cardiology     | Heart Surgery     | ABC12345       |
| shiv456   | password456 | MALE   | Shiva      | Pandey    | shivam456.smith@example.com | 9876543211 | Neurology      | Brain Surgery     | DEF67890       |
| sam789    | password789 | FEMALE | Samira     | Singh     | sameera@example.com         | 9876543212 | Orthopedics    | Joint Replacement | GHI11223       |
| pankaj101 | password101 | MALE   | Pankaj     | Dubey     | dubey.pankaj@example.com    | 9876543213 | Pediatrics     | Pediatric Surgery | JKL33445       |

### How to Use:
1. Download the sample file.
2. Fill in the doctor information as per the required format.
3. Upload the Excel file to the system to register the doctors.


## Current Status

🚧 **Project Status**: _In Progress_

The core features of the system are actively being developed and tested. Expect ongoing updates as new features are added and existing ones are refined. Please check back for future updates as we move towards a complete version.

## Future Plans

Once completed, the Hospital Management System will offer:

- **Comprehensive Dashboard** for hospital administrators and managers
- **Detailed Analytics** for data-driven insights
- **Patient Portal** for viewing appointments and medical history
- **Integration with Third-Party Systems** for expanded functionality
- **Aadhaar Authentication** for added security and details fetching
- **Medical Records** making a centralized platform for storing all the records in an encrypted way for easy accessibility.

## Contribution Guidelines 🤝
As the project is still under active development, we welcome feedback and suggestions to help shape its direction. Contributions at this stage are focused on ideas and suggestions that could enhance the project's functionality.

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

Your contributions will help enhance functionality, add new features, improve code quality, and fix bugs. Thank you for considering contributing!

---

Thank you for checking out this project! Stay tuned for more updates as we continue development! 🎉

