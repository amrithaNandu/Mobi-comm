package com.backend.mobicomm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mobicomm.model.Payment;
import com.backend.mobicomm.model.Plan;
import com.backend.mobicomm.model.Recharge;
import com.backend.mobicomm.model.User;
import com.backend.mobicomm.repository.UserRepository;
import com.backend.mobicomm.security.JwtUtil;
import com.backend.mobicomm.service.PlanService;
import com.backend.mobicomm.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired 
    private PlanService planService;

    @GetMapping("/check")
    public ResponseEntity<?> checkUser(@RequestParam String mobileNumber) {
        boolean isMobiCommUser = userService.isMobiCommUser(mobileNumber);
        return ResponseEntity.ok().body(Map.of("message", "User found"));
    }
    
    @PostMapping("/assign-plan")
    public ResponseEntity<?> assignPlanToUser(@RequestParam String mobileNumber, @RequestParam Long planId) {
        User user = userService.assignPlanToUser(mobileNumber, planId);
        return ResponseEntity.ok().body(Map.of("message", "Plan assigned successfully", "user", user));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(
            @RequestParam String mobileNumber,
            @RequestParam Long planId,
            @RequestParam String paymentMethod,
            @RequestParam double amount,
            @RequestParam String transactionId) {
        Payment payment = userService.processPayment(mobileNumber, planId, paymentMethod, amount, transactionId);
        return ResponseEntity.ok().body(Map.of("message", "Payment processed successfully", "paymentId", payment.getPaymentId()));
    }

    @PostMapping("/create-recharge")
    public ResponseEntity<?> createRecharge(
            @RequestParam String mobileNumber,
            @RequestParam Long planId,
            @RequestParam String paymentId) {
        Recharge recharge = userService.createRecharge(mobileNumber, planId, paymentId);
        return ResponseEntity.ok().body(Map.of("message", "Recharge created successfully", "rechargeId", recharge.getRechargeId()));
    }
    
    @GetMapping("/expiring-plans")
    public ResponseEntity<?> getUsersWithExpiringPlans() {
        List<User> users = userService.getUsersWithExpiringPlans();
        users.forEach(user -> {
            Plan currentPlan = user.getCurrentPlan();
            if (currentPlan != null) {
                currentPlan.getId(); 
            }
        });
        return ResponseEntity.ok().body(users);
    }
    

}