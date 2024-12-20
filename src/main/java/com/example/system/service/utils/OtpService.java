package com.example.system.service.utils;

import com.example.system.dto.OtpData;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final long OTP_EXPIRATION_MINUTES = 5;

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public OtpData generateOtp(String key) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpStorage.put(key, new OtpData(otp, System.currentTimeMillis()));
        return new OtpData(otp, OTP_EXPIRATION_MINUTES);
    }

    public boolean validateOtp(String key, String otp) {
        OtpData otpData = otpStorage.get(key);
        if (otpData == null || isExpired(otpData.expiryTimeInMinutes())) {
            otpStorage.remove(key);
            return false;
        }
        boolean isValid = otpData.otp().equals(otp);
        if (isValid) {
            otpStorage.remove(key);
        }
        return isValid;
    }

    private boolean isExpired(long timestamp) {
        long currentTime = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toMinutes(currentTime - timestamp) >= OTP_EXPIRATION_MINUTES;
    }
}
