package com.backend.mobicomm.service;

import com.backend.mobicomm.model.Recharge;
import com.backend.mobicomm.repository.RechargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;

    public List<Recharge> getRechargeHistoryByUserId(String userId) {
        return rechargeRepository.findByUser_Userid(userId);
    }
    
    public List<Recharge> getAllRechargeHistory() {
        return rechargeRepository.findAll(); // Fetch all recharge records
    }
}