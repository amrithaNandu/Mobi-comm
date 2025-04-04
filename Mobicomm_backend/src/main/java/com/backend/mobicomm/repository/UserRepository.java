package com.backend.mobicomm.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.mobicomm.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByMobileNumber(String mobileNumber);
	Optional<User> findByEmail(String email);
	Optional findById(String currentPlan);
	
	Optional<User> findByMobileNumberAndEmail(String mobileNumber, String email);
	
	@Query(value = "SELECT * FROM users u  WHERE u.last_recharge_time IS NOT NULL AND u.current_plan_id IS NOT NULL AND DATE_ADD(u.last_recharge_time, INTERVAL (SELECT p.validity FROM plans p WHERE p.id = u.current_plan_id) DAY) <= :expiryDate", nativeQuery = true)
	List<User> findUsersWithExpiringPlans(@Param("expiryDate") LocalDateTime expiryDate);
}
