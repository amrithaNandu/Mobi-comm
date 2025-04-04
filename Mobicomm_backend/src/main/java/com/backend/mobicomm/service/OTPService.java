package com.backend.mobicomm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    public void storeOTP(String email, String otp) {
        email = email.toLowerCase(); // Normalize email to lowercase
        logger.info("Storing OTP for email: {}, OTP: {}", email, otp);
        otpStorage.put(email, otp);
    }

    public String getOTP(String email) {
        email = email.toLowerCase(); // Normalize email to lowercase
        String otp = otpStorage.get(email);
        logger.info("Retrieved OTP for email: {}, OTP: {}", email, otp);
        return otp;
    }

    public void clearOTP(String email) {
        email = email.toLowerCase(); // Normalize email to lowercase
        logger.info("Clearing OTP for email: {}", email);
        otpStorage.remove(email);
    }

    public boolean verifyOTP(String email, String otp) {
        email = email.toLowerCase(); // Normalize email to lowercase
        String storedOTP = getOTP(email);
        boolean isValid = storedOTP != null && storedOTP.equals(otp);
        logger.info("Verifying OTP for email: {}, OTP: {}, Valid: {}", email, otp, isValid);
        return isValid;
    }
}