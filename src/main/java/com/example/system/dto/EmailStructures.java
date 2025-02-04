package com.example.system.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailStructures {

    @Value("${hospital.support.email}")
    private String supportEmail;

    @Value("${hospital.support.phone}")
    private String supportPhone;

    @Value("${hospital.website.url}")
    private String websiteUrl;

    // =================== OTP EMAIL ===================
    public String generateOtpEmail(String otp, long validTime) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <h3 style="color: #007bff;">OTP Verification</h3>
                    <p>Dear User,</p>
                    <p>Your <b>One-Time Password (OTP)</b> for AyuMed is:
                        <span style="font-size: 18px; font-weight: bold; color: #d9534f;">%s</span></p>
                    <p>This OTP is valid for <b>%d minutes</b>.</p>
                    <p style="color: red;"><b>Do not share this OTP</b> with anyone.</p>
                
                    <hr>
                
                    <p>Warm regards,</p>
                    <p><b>The AyuMed Team</b></p>

                    <p><b>Contact Us:</b></p>
                    <p>Email: <a href="mailto:%s">%s</a></p>
                    <p>Phone: %s</p>
                    <p>Website: <a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(otp, validTime, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== REGISTRATION EMAIL ===================
    public String generateRegistrationEmail(String name) {
        return """
                <html>
                <body>
                    <h3 style="color: #28a745;">Welcome to AyuMed 🎉</h3>
                    <p>Dear <b>%s</b>,</p>
                    <p>We are thrilled to welcome you to <b>AyuMed</b>, where healthcare meets convenience.</p>
                
                    <h4>With AyuMed, you can:</h4>
                    <ul>
                        <li>✅ Book appointments with top doctors.</li>
                        <li>✅ Access your medical records securely.</li>
                        <li>✅ Stay updated with health tips and services.</li>
                    </ul>

                    <p>Thank you for choosing AyuMed. We look forward to being your trusted healthcare partner.</p>

                    <hr>
                
                    <p>Warm regards,</p>
                    <p><b>The AyuMed Team</b></p>

                    <p><b>Contact Us:</b></p>
                    <p>Email: <a href="mailto:%s">%s</a></p>
                    <p>Phone: %s</p>
                    <p>Website: <a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(name, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT CONFIRMATION (PATIENT) ===================
    public String generateAppointmentConfirmationPatient(String patientName, String doctorName, String specialty,
                                                         String appointmentDate, String appointmentTime, String location) {
        return """
                <html>
                <body>
                    <h3 style="color: #17a2b8;">Appointment Confirmation</h3>
                    <p>Dear <b>%s</b>,</p>
                    <p>Your appointment has been successfully booked.</p>
                
                    <h4>🗓 Appointment Details:</h4>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s, %s</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>

                    <p>If you need to <b>reschedule or cancel</b>, please log in to your AyuMed account or contact our support team.</p>

                    <hr>
                
                    <p>Warm regards,</p>
                    <p><b>The AyuMed Team</b></p>

                    <p><b>Contact Us:</b></p>
                    <p>Email: <a href="mailto:%s">%s</a></p>
                    <p>Phone: %s</p>
                    <p>Website: <a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(patientName, doctorName, specialty, appointmentDate, appointmentTime, location, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT CONFIRMATION (DOCTOR) ===================
    public String generateAppointmentConfirmationDoctor(String doctorName, String patientName, String department,
                                                        String appointmentDate, String appointmentTime, String location) {
        return """
                <html>
                <body>
                    <h3 style="color: #ff9800;">New Appointment Scheduled</h3>
                    <p>Dear Dr. <b>%s</b>,</p>
                    <p>A new appointment has been scheduled for you.</p>
                
                    <h4>📅 Appointment Details:</h4>
                    <ul>
                        <li><b>Patient Name:</b> %s</li>
                        <li><b>Department:</b> %s</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>

                    <p>Please log in to your AyuMed portal to review patient details and prepare for the appointment.</p>

                    <hr>
                
                    <p>Warm regards,</p>
                    <p><b>The AyuMed Team</b></p>

                    <p><b>Contact Us:</b></p>
                    <p>Email: <a href="mailto:%s">%s</a></p>
                    <p>Phone: %s</p>
                    <p>Website: <a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(doctorName, patientName, department, appointmentDate, appointmentTime, location, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT STATUS UPDATE (PATIENT) ===================
    public String generateAppointmentStatusUpdatePatient(String patientName, String doctorName, String specialty,
                                                         String appointmentDate, String appointmentTime, String location,
                                                         String status) {
        return """
                <html>
                <body>
                    <h3 style="color: #17a2b8;">Appointment Status Update</h3>
                    <p>Dear <b>%s</b>,</p>
                    <p>Your appointment with <b>Dr. %s</b> has been <b style="color: #d9534f;">%s</b>.</p>
                
                    <h4>🗓 Updated Appointment Details:</h4>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s, %s</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>

                    <p>If you have any questions, please contact our support team.</p>

                    <hr>
                
                    <p>Warm regards,</p>
                    <p><b>The AyuMed Team</b></p>

                    <p><b>Contact Us:</b></p>
                    <p>Email: <a href="mailto:%s">%s</a></p>
                    <p>Phone: %s</p>
                    <p>Website: <a href="%s">%s</a></p>
                </body>
                </html>
                """.formatted(patientName, doctorName, status, doctorName, specialty, appointmentDate, appointmentTime, location,
                supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT STATUS CANCEL (PATIENT) ===================
    public String generateAppointmentCancellationPatient(String patientName, String doctorName,
                                                         String specialty, String appointmentDate,
                                                         String appointmentTime, String location,
                                                         String reason) {
        return """
            <html>
            <body>
                <h3 style="color: #d9534f;">Appointment Cancellation</h3>
                <p>Dear <b>%s</b>,</p>
                <p>We regret to inform you that your upcoming appointment with <b>Dr. %s</b> has been canceled.</p>

                <h4>❌ Canceled Appointment Details:</h4>
                <ul>
                    <li><b>Doctor:</b> Dr. %s, %s</li>
                    <li><b>Date:</b> %s</li>
                    <li><b>Time:</b> %s</li>
                    <li><b>Location:</b> %s</li>
                    <li><b>Reason:</b> %s</li>
                </ul>

                <p>If you wish to reschedule, please log in to your AyuMed account or contact our support team.</p>

                <hr>
            
                <p>We apologize for any inconvenience caused.</p>
            
                <p>Warm regards,</p>
                <p><b>The AyuMed Team</b></p>

                <p><b>Contact Us:</b></p>
                <p>Email: <a href="mailto:%s">%s</a></p>
                <p>Phone: %s</p>
                <p>Website: <a href="%s">%s</a></p>
            </body>
            </html>
            """.formatted(patientName, doctorName, doctorName, specialty, appointmentDate,
                appointmentTime, location, reason, supportEmail, supportEmail, supportPhone,
                websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT STATUS CANCEL (DOCTOR) ===================
    public String generateAppointmentCancellationDoctor(String doctorName, String patientName,
                                                        String department, String appointmentDate,
                                                        String appointmentTime, String location,
                                                        String reason) {
        return """
            <html>
            <body>
                <h3 style="color: #d9534f;">Appointment Cancellation</h3>
                <p>Dear Dr. <b>%s</b>,</p>
                <p>We regret to inform you that the following appointment has been canceled:</p>

                <h4>❌ Canceled Appointment Details:</h4>
                <ul>
                    <li><b>Patient:</b> %s</li>
                    <li><b>Department:</b> %s</li>
                    <li><b>Date:</b> %s</li>
                    <li><b>Time:</b> %s</li>
                    <li><b>Location:</b> %s</li>
                    <li><b>Reason:</b> %s</li>
                </ul>

                <p>Please check your AyuMed portal for further details.</p>

                <hr>
            
                <p>We apologize for any inconvenience caused.</p>
            
                <p>Warm regards,</p>
                <p><b>The AyuMed Team</b></p>

                <p><b>Contact Us:</b></p>
                <p>Email: <a href="mailto:%s">%s</a></p>
                <p>Phone: %s</p>
                <p>Website: <a href="%s">%s</a></p>
            </body>
            </html>
            """.formatted(doctorName, patientName, department, appointmentDate,
                appointmentTime, location, reason, supportEmail, supportEmail, supportPhone,
                websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT REMINDER (PATIENT) ===================
    public String generateAppointmentReminderPatient(String patientName, String doctorName,
                                                     String specialty, String appointmentDate,
                                                     String appointmentTime, String location) {
        return """
            <html>
            <body>
                <h3 style="color: #17a2b8;">Appointment Reminder</h3>
                <p>Dear <b>%s</b>,</p>
                <p>This is a friendly reminder from <b>AyuMed</b> that you have an upcoming appointment.</p>

                <h4>📅 Appointment Details:</h4>
                <ul>
                    <li><b>Doctor:</b> Dr. %s, %s</li>
                    <li><b>Date:</b> %s</li>
                    <li><b>Time:</b> %s</li>
                    <li><b>Location:</b> %s</li>
                </ul>

                <p>Please arrive <b>10 minutes early</b> for in-person appointments.</p>
                <p>For <b>video consultations</b>, ensure a stable internet connection and log in at least 5 minutes before the scheduled time.</p>

                <p>If you need to reschedule or cancel, please log in to your AyuMed account or contact our support team.</p>

                <hr>
            
                <p>We look forward to serving you!</p>
            
                <p>Warm regards,</p>
                <p><b>The AyuMed Team</b></p>

                <p><b>Contact Us:</b></p>
                <p>Email: <a href="mailto:%s">%s</a></p>
                <p>Phone: %s</p>
                <p>Website: <a href="%s">%s</a></p>
            </body>
            </html>
            """.formatted(patientName, doctorName, specialty, appointmentDate,
                appointmentTime, location, supportEmail, supportEmail, supportPhone,
                websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT REMINDER (DOCTOR) ===================
    public String generateAppointmentReminderDoctor(String doctorName, String patientName,
                                                    String department, String appointmentDate,
                                                    String appointmentTime, String location) {
        return """
            <html>
            <body>
                <h3 style="color: #ff9800;">Upcoming Appointment Reminder</h3>
                <p>Dear Dr. <b>%s</b>,</p>
                <p>This is a reminder about your upcoming appointment with <b>%s</b>.</p>

                <h4>📅 Appointment Details:</h4>
                <ul>
                    <li><b>Patient:</b> %s</li>
                    <li><b>Department:</b> %s</li>
                    <li><b>Date:</b> %s</li>
                    <li><b>Time:</b> %s</li>
                    <li><b>Location:</b> %s</li>
                </ul>

                <p>Please ensure you are prepared for the consultation and check the AyuMed portal for any additional details.</p>

                <hr>
            
                <p>Thank you for your dedication to patient care!</p>
            
                <p>Warm regards,</p>
                <p><b>The AyuMed Team</b></p>

                <p><b>Contact Us:</b></p>
                <p>Email: <a href="mailto:%s">%s</a></p>
                <p>Phone: %s</p>
                <p>Website: <a href="%s">%s</a></p>
            </body>
            </html>
            """.formatted(doctorName, patientName, patientName, department, appointmentDate,
                appointmentTime, location, supportEmail, supportEmail, supportPhone,
                websiteUrl, websiteUrl);
    }

}