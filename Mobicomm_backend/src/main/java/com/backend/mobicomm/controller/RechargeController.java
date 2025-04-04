package com.backend.mobicomm.controller;

import com.backend.mobicomm.model.Recharge;
import com.backend.mobicomm.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @GetMapping("/history")
    public List<Recharge> getAllRechargeHistory() {
        return rechargeService.getAllRechargeHistory();
    }
}