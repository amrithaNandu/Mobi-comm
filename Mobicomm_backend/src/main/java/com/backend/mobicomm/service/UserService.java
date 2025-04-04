package com.backend.mobicomm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.backend.mobicomm.exception.UserNotFoundException;
import com.backend.mobicomm.model.Payment;
import com.backend.mobicomm.model.Plan;
import com.backend.mobicomm.model.Recharge;
import com.backend.mobicomm.model.User;
import com.backend.mobicomm.repository.PaymentRepository;
import com.backend.mobicomm.repository.PlanRepository;
import com.backend.mobicomm.repository.RechargeRepository;
import com.backend.mobicomm.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RechargeRepository rechargeRepository;

    public boolean isMobiCommUser(String mobileNumber) {
        Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
        if (user.isPresent()) {
            return true;
        } else {
            throw new UserNotFoundException("Please enter valid mobi-comm number");
        }
    }
    
 
    public boolean isMobiCommUser(String mobileNumber, String email) {
        return userRepository.findByMobileNumberAndEmail(mobileNumber, email).isPresent();
    }

    public User assignPlanToUser(String mobileNumber, Long planId) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        user.setCurrentPlan(plan); 
        return userRepository.save(user);
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public Payment processPayment(String mobileNumber, Long planId, String paymentMethod, double amount, String transactionId) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPlan(plan);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setTransactionId(transactionId);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("SUCCESS"); 

        return paymentRepository.save(payment);
    }

    public Recharge createRecharge(String mobileNumber, Long planId, String paymentId) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Recharge recharge = new Recharge();
        recharge.setUser(user);
        recharge.setPlan(plan);
        recharge.setPayment(payment);
        recharge.setRechargeTime(LocalDateTime.now());
        recharge.setStatus("ACTIVE");
        user.setLastRechargeTime(LocalDateTime.now());
        userRepository.save(user);
        return rechargeRepository.save(recharge);
    }
    
    public List<User> getUsersWithExpiringPlans() {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(3); 
        List<User> users = userRepository.findUsersWithExpiringPlans(expiryDate);
        users.forEach(user -> {
            // Ensure the currentPlan is initialized
            if (user.getCurrentPlan() != null) {
                user.getCurrentPlan().getId(); // Trigger lazy loading
            }
        });
        return users;
    }

	public Optional<User> findByEmail(String email) 
	{
		return userRepository.findByEmail(email);
	}
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

	    return org.springframework.security.core.userdetails.User
	            .withUsername(user.getEmail())
	            .password("") 
	            .authorities("USER") 
	            .build();
	}

}
