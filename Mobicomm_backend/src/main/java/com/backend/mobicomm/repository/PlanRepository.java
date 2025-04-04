package com.backend.mobicomm.repository;

import com.backend.mobicomm.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByIsPopularTrueAndIsActiveTrue();
    List<Plan> findByIsPopularFalseAndIsActiveTrue();
    List<Plan> findByCategoryAndIsActiveTrue(String category);
}