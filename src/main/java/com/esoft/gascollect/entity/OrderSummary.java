package com.esoft.gascollect.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "order_summary")
public class OrderSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer details
    private String customerName;
    private String email;
    private String mobileNumber;
    private Long userId;

    // Gas selection details
    private Long gasId;
    private Boolean hasEmptyCylinder;
    private String location;
    private String outletId;
    private Integer quantity;

    // Delivery location details
    private String addressLine1;
    private String addressLine2;
    private String city;
    private LocalDate deliveryDate;
    private String deliveryTime;
    private String postalCode;

    // Payment details
    private String paymentMethod;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String nameOnCard;
}