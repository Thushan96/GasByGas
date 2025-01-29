package com.esoft.gascollect.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderSummaryDTO {
    private Long id;
    private String customerName;
    private String email;
    private String mobileNumber;
    private Long userId;
    private Long gasId;
    private Boolean hasEmptyCylinder;
    private String location;
    private String outletId;
    private Integer quantity;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private LocalDate deliveryDate;
    private String deliveryTime;
    private String postalCode;
    private String paymentMethod;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String nameOnCard;

   }