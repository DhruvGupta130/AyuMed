package com.example.system.dto;

public class EmailStructures {

    public static String scheduleAppointmentPatientBody(String patientName, String doctorName,
                                                        String specialty, String appointmentDate,
                                                        String appointmentTime, String location){
        return """
                Dear %s,
                
                Thank you for choosing AyuMed for your healthcare needs. We are pleased to confirm that your appointment has been successfully booked.
                
                Appointment Details:
                Doctor: Dr. %s, %s
                Date & Time: %s at %s
                Location: %s
                If you need to cancel or reschedule, please log in to your AyuMed account or contact our support team at [Support Email] at least 24 hours in advance.
                
                Important Notes:
                Please arrive 10 minutes early for in-person appointments.
                For video consultations, ensure a stable internet connection and log in at least 5 minutes before the scheduled time.
                Thank you for trusting AyuMed with your care. We look forward to providing you with the highest quality of service.
                
                Warm regards,
                The AyuMed Team
                [Contact Information]
                [Website URL]
               
                """.formatted(patientName, doctorName, specialty, appointmentDate, appointmentTime, location);
    }

    public static String scheduleAppointmentDoctorBody(String doctorName, String patientName,
                                                       String department, String appointmentDate,
                                                       String appointmentTime, String location){
        return """
                Dear Dr. %s,
                
                We are pleased to inform you that a new appointment has been scheduled for you with the following details:
                
                Patient Name: %s
                Department: %s
                Appointment Date & Time: %s at %s
                Location: %s
                
                Please take a moment to review the patient’s information on your AyuMed portal. Should you have any questions or require additional details, feel free to reach out to our administrative team.
                
                Thank you for your dedication to providing excellent care.
                
                Warm regards,
                The AyuMed Team
                [Support Email]
                [Contact Phone Number]
                [Website URL]
                """.formatted(doctorName, patientName, department, appointmentDate, appointmentTime, location);
    }

    public static String setAppointmentReminder(String patientName, String doctorName,
                                         String specialty, String appointmentDate,
                                         String appointmentTime, String location){
        return """
                Dear %s,
                
                This is a friendly reminder from AyuMed that you have an upcoming appointment scheduled:
                
                Appointment Details:
                - Doctor: Dr. %s, %s
                - Date: %s
                - Time: %s
                - Location: %s
                
                If you need to reschedule or cancel your appointment, please do so at least 24 hours in advance by logging into your AyuMed account or contacting our support team at [Support Email].
                
                Important Notes:
                - Please arrive 10 minutes early for your appointment.
                - For video consultations, ensure you have a stable internet connection and log in at least 5 minutes before the scheduled time.
                
                Thank you for choosing AyuMed for your healthcare needs. We look forward to seeing you!
                
                Warm regards,
                The AyuMed Team
                [Contact Information]
                [Website URL]
                """.formatted(patientName, doctorName, specialty, appointmentDate, appointmentTime, location);
    }

    public static String cancelAppointmentPatient(String patientName, String doctorName,
                                    String specialty, String appointmentDate,
                                    String appointmentTime, String location, String reason){
        return """
                Dear %s,
                
                We regret to inform you that your upcoming appointment with Dr. %s has been canceled.
                
                Appointment Details:
                
                Doctor: Dr. %s, %s
                Date: %s
                Time: %s
                Location: %s
                Cancellation Reason: %s
                
                If you wish to reschedule your appointment, please visit your AyuMed account or contact our support team at [Support Email].
                
                Thank you for your understanding, and we apologize for any inconvenience this may have caused.
                
                Best regards,
                The AyuMed Team
                """.formatted(patientName, doctorName, doctorName, specialty, appointmentDate, appointmentTime, location, reason);
    }

    public static String cancelAppointmentDoctor(String patientName, String doctorName,
                                                 String department, String appointmentDate,
                                                 String appointmentTime, String location, String reason){
        return """
                Dear Dr. %s,
                
                We would like to inform you that the following appointment has been canceled:
                
                Patient Name: %s
                Department: %s
                Original Appointment Date & Time: %s at %s \s
                Location: %s
                Cancellation Reason: %s
                
                Please log into your AyuMed portal if you require additional details or need to review other upcoming appointments.
                
                Thank you for your continued commitment to providing exceptional care.
                
                Best regards,
                The AyuMed Team
                [Support Email]
                [Contact Phone Number]
                [Website URL]
                """.formatted(doctorName, patientName, department, appointmentDate, appointmentTime, location, reason);
    }

    public static String appointmentStatusUpdatedPatient(String patientName, String doctorName,
                                            String specialty, String appointmentDate,
                                            String appointmentTime, String location, String status){
        return """
                Dear %s,
                
                We are pleased to inform you that your appointment with Dr. %s has been successfully %s.
                
                Appointment Details:
                
                Doctor: Dr. %s, %s
                Date: %s
                Time: %s
                Location: %s
                If you have any questions or need to reschedule, please feel free to contact our support team at [Support Email].
                
                Thank you for choosing AyuMed. We look forward to seeing you!
                
                Best regards,
                The AyuMed Team
                """.formatted(patientName, doctorName, status, doctorName, specialty, appointmentDate, appointmentTime, location);
    }

    public static String appointmentStatusUpdatedDoctor(String doctorName, String patientName,
                                                        String appointmentDate, String appointmentTime,
                                                        String status){
        return """
                Dear Dr. %s,
                
                We are pleased to inform you that, as per your request, the status of your appointment has been successfully updated to %s.
                
                Appointment Details:
                Patient: %s
                Date: %s
                Time: %s
                Updated Status: %s
                If you have any questions or need further assistance, please do not hesitate to contact our support team at [Support Email].
                
                Thank you for your dedication and for choosing AyuMed. We appreciate your commitment to providing exceptional care.
                
                Best regards,
                
                The AyuMed Team
                """.formatted(doctorName, status, patientName, appointmentDate, appointmentTime, status);
    }

    public static String registrationEmailBody(String patientName) {
        return """
            Dear %s,
        
            Congratulations on registering with AyuMed! 🎉
            We are thrilled to welcome you to our platform, where healthcare meets convenience.
        
            With AyuMed, you can:
            - Book appointments with top doctors.
            - Access your medical records securely.
            - Stay updated with health tips and services.
        
            Thank you for choosing AyuMed. We look forward to being your trusted healthcare partner.
        
            Warm regards,
            The AyuMed Team
        
            **Contact Us:**
            If you have any questions, feel free to reach out to our support team at {supportEmail} or call us at {supportPhone}.
            """.formatted(patientName);

    }
}
