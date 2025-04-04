package com.backend.mobicomm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.mobicomm.model.Admin;
import com.backend.mobicomm.repository.AdminRepository;

@Service
public class AdminService implements UserDetailsService {
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Admin saveAdmin(String email, String password) {
        // Validate password strength
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEnabled(true); // Enable admin by default
        return adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        if (!admin.isEnabled()) {
            throw new DisabledException("Admin account is disabled");
        }

        return User.withUsername(admin.getEmail())
                   .password(admin.getPassword())
                   .roles("ADMIN")
                   .accountLocked(false) // You can implement account locking logic here
                   .build();
    }
    
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}