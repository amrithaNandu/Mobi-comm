package com.backend.mobicomm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.mobicomm.model.Payment;
import com.backend.mobicomm.model.Recharge;

public interface PaymentRepository extends JpaRepository<Payment,String>{

}
