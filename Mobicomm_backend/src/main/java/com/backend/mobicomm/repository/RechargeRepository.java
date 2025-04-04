package com.backend.mobicomm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.mobicomm.model.Recharge;

public interface RechargeRepository extends JpaRepository<Recharge, String> 
{
	 List<Recharge> findByUser_Userid(String userId);
}