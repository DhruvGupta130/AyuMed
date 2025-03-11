package com.example.system.service.utils;

import com.example.system.dto.EmailStructures;
import com.example.system.dto.OtpData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OtpService {

    private static final long OTP_EXPIRATION_MINUTES = 5;
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    private final EmailService emailService;
    private final EmailStructures emailStructures;


    public void generateAndSendOtp(String key, String subject) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        long expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(OTP_EXPIRATION_MINUTES);
        OtpData otpData = new OtpData(otp, expiryTime);
        otpStorage.put(key, otpData);
        emailService.sendEmail(key, subject, emailStructures.generateOtpEmail(otp, OTP_EXPIRATION_MINUTES));
    }

    public boolean validateOtp(String key, String otp) {
        OtpData otpData = otpStorage.get(key);
        if (otpData == null || isExpired(otpData.expiryTime())) {
            otpStorage.remove(key);
            return true;
        }

        boolean isValid = otpData.otp().equals(otp);
        if (isValid) {
            otpStorage.remove(key);
        }
        return !isValid;
    }

    private boolean isExpired(long expiryTimestamp) {
        return System.currentTimeMillis() > expiryTimestamp;
    }
}