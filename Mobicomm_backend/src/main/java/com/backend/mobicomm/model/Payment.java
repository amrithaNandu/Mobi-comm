package com.backend.mobicomm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;
import com.backend.mobicomm.generator.PaymentIdGenerator;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(generator = "custom_payment_id")
    @GenericGenerator(name = "custom_payment_id", type = PaymentIdGenerator.class)
    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userid")
    @JsonBackReference // Add this annotation
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @JsonBackReference // Add this annotation
    private Plan plan;

    private String paymentMethod;
    private double amount;
    private LocalDateTime paymentTime;
    private String transactionId;
    private String status;
}