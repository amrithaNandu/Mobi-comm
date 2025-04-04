package com.backend.mobicomm.service;

import com.backend.mobicomm.model.Plan;
import com.backend.mobicomm.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> getPopularPlans() {
        return planRepository.findByIsPopularTrueAndIsActiveTrue();
    }

    public List<Plan> getPlansByCategory(String category) {
        return planRepository.findByCategoryAndIsActiveTrue(category);
    }

    public List<Plan> getNonPopularPlans() {
        return planRepository.findByIsPopularFalseAndIsActiveTrue();
    }

    public Plan getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
    }

    public Plan updatePlan(Long id, Plan updatedPlan) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
        plan.setData(updatedPlan.getData());
        plan.setValidity(updatedPlan.getValidity());
        plan.setCalls(updatedPlan.getCalls());
        plan.setSms(updatedPlan.getSms());
        plan.setPrice(updatedPlan.getPrice());
        return planRepository.save(plan);
    }

    public Plan createPlan(Plan newPlan) {
        return planRepository.save(newPlan);
    }
    public void togglePlanStatus(Long planId, boolean isActive) {
        Plan plan = planRepository.findById(planId)
                                  .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setActive(isActive);  
        planRepository.save(plan);
    }

}