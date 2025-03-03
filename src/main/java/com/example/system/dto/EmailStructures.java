package com.example.system.dto;

import com.example.system.entity.Medication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailStructures {

    @Value("${hospital.support.email}")
    private String supportEmail;

    @Value("${hospital.support.phone}")
    private String supportPhone;

    @Value("${hospital.website.url}")
    private String websiteUrl;

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    // =================== OTP EMAIL ===================
    public String generateOtpEmail(String otp, long validTime) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                <h2 style="color: #007bff;">🔐 OTP Verification</h2>

                <p>Dear User,</p>
                <p>Your <b>One-Time Password (OTP)</b> for AyuMed is:</p>

                <p style="font-size: 22px; font-weight: bold; color: #d9534f; background-color: #f8d7da; display: inline-block; padding: 10px 20px; border-radius: 5px;">%s</p>

                <p>This OTP is valid for <b>%d minutes</b>.</p>
                <p style="color: red; font-weight: bold;">⚠ Do not share this OTP with anyone for security reasons.</p>

                <p>If you did not request this OTP, please ignore this email or contact our support team immediately.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(otp, validTime, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }


    // =================== PATIENT REGISTRATION EMAIL ===================
    public String generateRegistrationEmail(String name) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                <h2 style="color: #28a745;">🎉 Welcome to AyuMed!</h2>

                <p>Dear <b>%s</b>,</p>
                <p>We are thrilled to have you join <b>AyuMed</b>, where healthcare meets convenience.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd; text-align: left;">
                    <h3 style="color: #007bff;">🚀 With AyuMed, you can:</h3>
                    <ul>
                        <li>✅ Book appointments with top doctors.</li>
                        <li>✅ Securely access your medical records anytime.</li>
                        <li>✅ Stay informed with health tips and updates.</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;">Thank you for choosing AyuMed. We look forward to being your trusted healthcare partner.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(name, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }


    // =================== HOSPITAL REGISTRATION EMAIL ===================
    public String generateHospitalWelcomeEmail(String hospitalName) {
        return """
        <html>
        <body>
            <h3 style="color: #28a745;">Welcome to AyuMed! 🎉</h3>
            <p>Dear <b>%s</b>,</p>
            <p>We are thrilled to welcome you to <b>AyuMed</b>, a platform that connects hospitals with patients efficiently.</p>

            <h4>🚀 What You Can Do:</h4>
            <ul>
                <li>✅ List your hospital's departments and doctors.</li>
                <li>✅ Manage OPD appointments seamlessly.</li>
                <li>✅ Improve patient engagement with advanced features.</li>
            </ul>

            <p>Your hospital profile is now live. Log in to your dashboard to start managing your services.</p>

            <p>If you need any assistance, feel free to reach out to our support team.</p>

            <hr>

            <p>Warm regards,</p>
            <p><b>The AyuMed Team</b></p>

            <p><b>Contact Us:</b></p>
            <p>Email: <a href="mailto:%s">%s</a></p>
            <p>Phone: %s</p>
            <p>Website: <a href="%s">%s</a></p>
        </body>
        </html>
        """.formatted(hospitalName, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== PHARMACY REGISTRATION EMAIL ===================
    public String generatePharmacyWelcomeEmail(String pharmacyName) {
        return """
        <html>
        <body>
            <h3 style="color: #007bff;">Welcome to AyuMed Pharmacy Network! 🎉</h3>
            <p>Dear <b>%s</b>,</p>
            <p>We are excited to have you on board as a trusted pharmacy partner on <b>AyuMed</b>.</p>

            <h4>🔹 What You Can Do:</h4>
            <ul>
                <li>✅ List and manage your available medications.</li>
                <li>✅ Provide online prescription fulfillment services.</li>
                <li>✅ Offer seamless ordering and delivery options.</li>
            </ul>

            <p>Your pharmacy profile is now live. Log in to your dashboard to start managing your services.</p>

            <p>If you need any assistance, feel free to reach out to our support team.</p>

            <hr>

            <p>Best regards,</p>
            <p><b>The AyuMed Team</b></p>

            <p><b>Contact Us:</b></p>
            <p>Email: <a href="mailto:%s">%s</a></p>
            <p>Phone: %s</p>
            <p>Website: <a href="%s">%s</a></p>
        </body>
        </html>
        """.formatted(pharmacyName, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== DOCTOR REGISTRATION EMAIL ===================
    public String generateDoctorWelcomeEmail(String doctorName, String hospitalName, String username, String password) {
        return """
        <html>
        <body>
            <h3 style="color: #007bff;">Welcome to %s – Your Account is Ready! 🎉</h3>
            <p>Dear Dr. <b>%s</b>,</p>
            <p>We are pleased to welcome you to <b>%s</b>. Your expertise is invaluable to us, and we look forward to your contributions in providing exceptional patient care.</p>

            <h4>🩺 Your Hospital Dashboard Access</h4>
            <p>Your account has been created, and you can now log in to manage appointments, patient records, and more.</p>

            <h4>🔐 Login Credentials:</h4>
            <p><b>Username:</b> %s</p>
            <p><b>Password:</b> %s</p>
            <p>For security reasons, we strongly recommend changing your password upon first login.</p>

            <h4>📢 Next Steps:</h4>
            <ul>
                <li>✅ Log in to your dashboard and update your profile.</li>
                <li>✅ Add your working schedules.</li>
                <li>✅ Review your appointment schedule.</li>
                <li>✅ Familiarize yourself with the hospital's digital systems.</li>
            </ul>

            <p>If you have any questions or need assistance, our support team is here to help.</p>

            <hr>

            <p>Best regards,</p>
            <p><b>The %s Administration Team</b></p>

            <p><b>Contact Support:</b></p>
            <p>Email: <a href="mailto:%s">%s</a></p>
            <p>Phone: %s</p>
            <p>Login Portal: <a href="%s">%s</a></p>
        </body>
        </html>
        """.formatted(hospitalName, doctorName, hospitalName, username, password, hospitalName, supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }

    // =================== APPOINTMENT CONFIRMATION (PATIENT) ===================
    public String generateAppointmentConfirmationPatient(String patientName, String doctorName, String specialty,
                                                         String appointmentDate, String appointmentTime, String location) {

        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #17a2b8; text-align: center;">Appointment Confirmation ✅</h2>
        
                <p>Dear <b>%s</b>,</p>
                <p>Your appointment has been successfully scheduled.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #28a745;">📅 Appointment Details</h3>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s (%s)</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>📝 Important Notes:</b></p>
                <ul>
                    <li>⏳ Please arrive at least <b>10 minutes early</b> for in-person visits.</li>
                    <li>💻 For <b>online consultations</b>, ensure a stable internet connection and join the session <b>5 minutes before</b>.</li>
                    <li>❗ If you need to <b>reschedule or cancel</b>, please log in to your AyuMed account or contact our support team.</li>
                </ul>

                <p style="margin-top: 15px;">We look forward to providing you with the best healthcare experience.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Us:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(patientName, doctorName, specialty, appointmentDate, appointmentTime, location,
                supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }


    // =================== APPOINTMENT CONFIRMATION (DOCTOR) ===================
    public String generateAppointmentConfirmationDoctor(String doctorName, String patientName, String department,
                                                        String appointmentDate, String appointmentTime, String location) {
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #ff9800; text-align: center;">🩺 New Appointment Scheduled</h2>
        
                <p>Dear Dr. <b>%s</b>,</p>
                <p>A new appointment has been successfully scheduled for you.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #28a745;">📅 Appointment Details</h3>
                    <ul>
                        <li><b>Patient Name:</b> %s</li>
                        <li><b>Department:</b> %s</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>📝 Next Steps:</b></p>
                <ul>
                    <li>🔍 Review patient details and medical history in the <b>AyuMed Portal</b>.</li>
                    <li>📅 Ensure availability and prepare for the consultation.</li>
                    <li>❗ If you need to reschedule, please update your availability in the portal.</li>
                </ul>

                <p>If you have any questions, our support team is here to assist you.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Portal: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(doctorName, patientName, department, appointmentDate, appointmentTime, location,
                supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }


    // =================== APPOINTMENT STATUS UPDATE (PATIENT) ===================
    public String generateAppointmentStatusUpdatePatient(String patientName, String doctorName, String specialty,
                                                         String appointmentDate, String appointmentTime, String location,
                                                         String status) {
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #17a2b8; text-align: center;">🔔 Appointment Status Update</h2>

                <p>Dear <b>%s</b>,</p>
                <p>We would like to inform you that your appointment with <b>Dr. %s</b> has been <b style="color: #d9534f;">%s</b>.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #28a745;">📅 Updated Appointment Details</h3>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s (%s)</li>
                        <li><b>Date & Time:</b> %s at %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>📝 Next Steps:</b></p>
                <ul>
                    <li>📅 If your appointment was <b>rescheduled</b>, please note the new details above.</li>
                    <li>❌ If it was <b>canceled</b>, you may book a new appointment through your AyuMed account.</li>
                    <li>📞 For any concerns, feel free to reach out to our support team.</li>
                </ul>

                <p>We apologize for any inconvenience and appreciate your understanding.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Portal: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
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
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #d9534f; text-align: center;">❌ Appointment Cancellation</h2>

                <p>Dear <b>%s</b>,</p>
                <p>We regret to inform you that your upcoming appointment with <b>Dr. %s</b> has been <b style="color: #d9534f;">canceled</b>.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #dc3545;">📅 Canceled Appointment Details</h3>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s (%s)</li>
                        <li><b>Date:</b> %s</li>
                        <li><b>Time:</b> %s</li>
                        <li><b>Location:</b> %s</li>
                        <li><b>Reason:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>🔄 Next Steps:</b></p>
                <ul>
                    <li>📅 If you'd like to <b>reschedule</b>, please log in to your AyuMed account and select a new appointment slot.</li>
                    <li>📞 If you need assistance, feel free to contact our support team.</li>
                </ul>

                <p>We sincerely apologize for any inconvenience and appreciate your understanding.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
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
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #d9534f; text-align: center;">❌ Appointment Cancellation</h2>

                <p>Dear Dr. <b>%s</b>,</p>
                <p>We regret to inform you that the following appointment has been <b style="color: #d9534f;">canceled</b>.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #dc3545;">📅 Canceled Appointment Details</h3>
                    <ul>
                        <li><b>Patient:</b> %s</li>
                        <li><b>Department:</b> %s</li>
                        <li><b>Date:</b> %s</li>
                        <li><b>Time:</b> %s</li>
                        <li><b>Location:</b> %s</li>
                        <li><b>Reason:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>🔄 Next Steps:</b></p>
                <ul>
                    <li>📅 Please check your <b>AyuMed Portal</b> for updated availability.</li>
                    <li>📞 If you have concerns regarding this cancellation, contact our support team.</li>
                </ul>

                <p>We apologize for any inconvenience and appreciate your understanding.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Portal: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
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
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #17a2b8; text-align: center;">⏰ Appointment Reminder</h2>

                <p>Dear <b>%s</b>,</p>
                <p>This is a friendly reminder from <b>AyuMed</b> that you have an upcoming appointment.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #28a745;">📅 Appointment Details</h3>
                    <ul>
                        <li><b>Doctor:</b> Dr. %s (%s)</li>
                        <li><b>Date:</b> %s</li>
                        <li><b>Time:</b> %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>🔹 Important Instructions:</b></p>
                <ul>
                    <li>⏳ Please arrive at least <b>10 minutes early</b> for in-person visits.</li>
                    <li>💻 For <b>video consultations</b>, ensure a stable internet connection and log in at least <b>5 minutes before</b>.</li>
                    <li>📅 If you need to <b>reschedule or cancel</b>, please log in to your AyuMed account or contact our support team.</li>
                </ul>

                <p>We look forward to providing you with the best healthcare experience.</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
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
        appointmentTime = LocalTime.parse(appointmentTime, inputFormatter).format(outputFormatter);

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
                <h2 style="color: #ff9800; text-align: center;">⏰ Upcoming Appointment Reminder</h2>

                <p>Dear Dr. <b>%s</b>,</p>
                <p>This is a friendly reminder about your upcoming appointment with <b>%s</b>.</p>

                <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                    <h3 style="color: #28a745;">📅 Appointment Details</h3>
                    <ul>
                        <li><b>Patient:</b> %s</li>
                        <li><b>Department:</b> %s</li>
                        <li><b>Date:</b> %s</li>
                        <li><b>Time:</b> %s</li>
                        <li><b>Location:</b> %s</li>
                    </ul>
                </div>

                <p style="margin-top: 15px;"><b>📝 Next Steps:</b></p>
                <ul>
                    <li>📋 Please review the patient’s medical history and appointment details in the <b>AyuMed Portal</b>.</li>
                    <li>⏳ Ensure availability and arrive on time for the consultation.</li>
                    <li>📞 If you need to reschedule, update your availability in the portal.</li>
                </ul>

                <p>Thank you for your dedication to patient care. We appreciate your commitment!</p>

                <hr>

                <p style="text-align: center; font-size: 14px; color: #555;">
                    <b>The AyuMed Team</b> <br>
                    <b>Contact Support:</b> <br>
                    📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                    📞 Phone: %s <br>
                    🌐 Portal: <a href="%s" style="color: #007bff;">%s</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(doctorName, patientName, patientName, department, appointmentDate,
                appointmentTime, location, supportEmail, supportEmail, supportPhone,
                websiteUrl, websiteUrl);
    }

    // =================== MEDICATION PURCHASE BILL ===================
    public String generateMedicationBillEmail(String patientName, List<Medication> medications, double totalAmount) {
        if (medications == null || medications.isEmpty()) {
            throw new IllegalArgumentException("Medication list cannot be empty.");
        }

        String supportEmail = medications.getFirst().getPharmacy().getEmail();
        String supportPhone = medications.getFirst().getPharmacy().getMobile();
        String websiteUrl = medications.getFirst().getPharmacy().getWebsite();

        String pharmacyName = medications.getFirst().getPharmacy().getPharmacyName();
        String purchaseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        StringBuilder medicationRows = new StringBuilder();
        for (Medication med : medications) {
            medicationRows.append("<tr style='border-bottom: 1px solid #ddd;'>")
                    .append("<td style='padding: 8px;'>").append(med.getMedicationName()).append("</td>")
                    .append("<td style='padding: 8px; text-align: center;'>").append(med.getQuantity()).append("</td>")
                    .append("<td style='padding: 8px; text-align: right;'>₹").append(String.format("%.2f", med.getPrice())).append("</td>")
                    .append("</tr>");
        }

        return """
    <html>
    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
        <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
            <h2 style="color: #28a745; text-align: center;">🧾 Medication Purchase Invoice</h2>
    
            <p>Dear <strong>%s</strong>,</p>
            <p>Thank you for your purchase from <strong>%s</strong>. Below are your order details:</p>
    
            <div style="background-color: #fff; padding: 15px; border-radius: 6px; border: 1px solid #ddd;">
                <h3 style="color: #007bff;">📅 Order Details</h3>
                <ul>
                    <li><b>Purchase Date:</b> %s</li>
                </ul>

                <h3 style="color: #007bff;">🧾 Bill Summary</h3>
                <table style="width: 100%%; border-collapse: collapse; text-align: left;">
                    <tr style="background-color: #f8f9fa; border-bottom: 2px solid #ddd;">
                        <th style="padding: 10px;">Medication Name</th>
                        <th style="padding: 10px; text-align: center;">Quantity</th>
                        <th style="padding: 10px; text-align: right;">Cost</th>
                    </tr>
                    %s
                </table>
    
                <h3 style="text-align: right; color: #d9534f;">Total Amount: ₹%.2f</h3>
            </div>

            <p>Thank you for choosing AyuMed Pharmacy. We look forward to serving you again!</p>

            <hr>

            <p style="text-align: center; font-size: 14px; color: #555;">
                <b>The AyuMed Team</b> <br>
                📧 Email: <a href="mailto:%s" style="color: #007bff;">%s</a> <br>
                📞 Phone: %s <br>
                🌐 Website: <a href="%s" style="color: #007bff;">%s</a>
            </p>
        </div>
    </body>
    </html>
    """.formatted(patientName, pharmacyName, purchaseDate, medicationRows.toString(), totalAmount,
                supportEmail, supportEmail, supportPhone, websiteUrl, websiteUrl);
    }


}