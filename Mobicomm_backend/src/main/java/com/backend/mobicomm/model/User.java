package com.backend.mobicomm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import com.backend.mobicomm.generator.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "users", indexes = {
	    @Index(name = "idx_userid", columnList = "userid", unique = true) // Add this line
	})
public class User {

    @Id
    @GeneratedValue(generator = "custom_user_id")
    @GenericGenerator(name = "custom_user_id", type = CustomUserIdGenerator.class)
    private String userid;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    private String name;
    private String email;

    @Column(name = "sim_activated_time")
    private LocalDateTime simActivatedTime; 

    @ManyToOne 
    @JoinColumn(name = "current_plan_id", referencedColumnName = "id")
    @JsonBackReference
    private Plan currentPlan; 

    @Column(name = "is_active")
    @JsonProperty("active")
    private boolean isActive;

    @Column(name = "last_recharge_time")
    private LocalDateTime lastRechargeTime; 
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // Exclude from Lombok's toString() to avoid circular references
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // Exclude from Lombok's toString() to avoid circular references
    private List<Recharge> recharges;
}