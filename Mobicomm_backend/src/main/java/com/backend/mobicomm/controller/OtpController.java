package com.backend.mobicomm.controller;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mobicomm.security.JwtUtil;
import com.backend.mobicomm.service.EmailService;
import com.backend.mobicomm.service.OTPService;
import com.backend.mobicomm.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@RestController
@RequestMapping("/log")
public class OtpController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> request) {
        String mobileNumber = request.get("mobileNumber");
        String email = request.get("email");

        // Check if user exists with both mobile number and email
        boolean isMobiCommUser = userService.isMobiCommUser(mobileNumber, email);
        if (!isMobiCommUser) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User not found."));
        }

        // Generate OTP
        String otp = otpService.generateOTP();

        // Store OTP temporarily
        otpService.storeOTP(email, otp);

        // Send OTP via email
        emailService.sendOTPEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent successfully."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        System.out.println("Verifying OTP for email: " + email + ", OTP: " + otp);

        boolean isOTPValid = otpService.verifyOTP(email, otp);
        if (!isOTPValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid OTP."));
        }

        otpService.clearOTP(email);

        String accessToken = jwtUtil.generateToken(email, 1440); 

        return ResponseEntity.ok(Map.of("message", "Successfully logged in!", "accessToken", accessToken));
    }


    @PostMapping("/send-payment-success-email")
    public ResponseEntity<?> sendPaymentSuccessEmail(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        Map<String, String> planDetails = (Map<String, String>) request.get("plan");

        if (email == null || planDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and plan details are required"));
        }

        emailService.sendPaymentSuccessEmail(email, planDetails);

        return ResponseEntity.ok().body(Map.of("message", "Payment success email sent successfully"));
    }
}