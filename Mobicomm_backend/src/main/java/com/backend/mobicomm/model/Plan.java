package com.backend.mobicomm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "plans", indexes = {
	    @Index(name = "idx_plan_id", columnList = "id", unique = true) // Add this line
	})
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String newid;
    private String name;
    private double price;
    private String calls;
    private String data;
    private String sms;
    private int validity;
    private String entertainment;
    private String description;
    private String badge;
    private String category;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_popular")
    private boolean isPopular;

    @OneToMany(mappedBy = "currentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude 
    private List<User> users;

    // Bidirectional relationship with Payment
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude 
    private List<Payment> payments;

    // Bidirectional relationship with Recharge
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude 
    private List<Recharge> recharges;
}