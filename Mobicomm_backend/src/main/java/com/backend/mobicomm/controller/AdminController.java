package com.backend.mobicomm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.backend.mobicomm.model.Admin;
import com.backend.mobicomm.security.JwtUtil;
import com.backend.mobicomm.service.AdminService;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        try {
            // Authenticate the admin
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword())
            );

            // Generate tokens
            String accessToken = jwtUtil.generateToken(admin.getEmail(), 15); // 15 minutes
            String refreshToken = jwtUtil.generateToken(admin.getEmail(), 10080); // 7 days

            // Return tokens
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account locked");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account disabled");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            if (adminService.existsByEmail(admin.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists!");
            }
            
            Admin registeredAdmin = adminService.saveAdmin(admin.getEmail(), admin.getPassword());
            
            // Generate tokens for immediate login after registration
            String accessToken = jwtUtil.generateToken(registeredAdmin.getEmail(), 15);
            String refreshToken = jwtUtil.generateToken(registeredAdmin.getEmail(), 10080);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin registered successfully!");
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering admin: " + e.getMessage());
        }
    }
    
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            String token = authHeader.substring(7);
            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}