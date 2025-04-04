package com.backend.mobicomm.controller;

import com.backend.mobicomm.model.Plan;
import com.backend.mobicomm.repository.PlanRepository;
import com.backend.mobicomm.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;
    
    @GetMapping("/popular")
    public List<Plan> getPopularPlans() {
        return planService.getPopularPlans();
    }

    @GetMapping("/category/{category}")
    public List<Plan> getPlansByCategory(@PathVariable String category) {
        return planService.getPlansByCategory(category);
    }

    @GetMapping("/non-popular")
    public List<Plan> getNonPopularPlans() {
        return planService.getNonPopularPlans();
    }

    @PutMapping("/{planId}/toggle-active")
    public ResponseEntity<Void> togglePlanActiveStatus(@PathVariable Long planId, @RequestBody Map<String, Boolean> request) {
        boolean isActive = request.get("is_active");
        planService.togglePlanStatus(planId, isActive);
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity<Plan> addNewPlan(@RequestBody Plan newPlan) {
        Plan savedPlan = planService.createPlan(newPlan);
        return ResponseEntity.ok(savedPlan);
    }
}