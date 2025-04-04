package com.backend.mobicomm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;
import com.backend.mobicomm.generator.RechargeIdGenerator;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "recharges")
public class Recharge {

    @Id
    @GeneratedValue(generator = "custom_recharge_id")
    @GenericGenerator(name = "custom_recharge_id", type = RechargeIdGenerator.class)
    private String rechargeId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userid")
    @JsonBackReference // Add this annotation
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @JsonBackReference // Add this annotation
    private Plan plan;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "paymentId")
    private Payment payment;

    private LocalDateTime rechargeTime;
    private String status;
}