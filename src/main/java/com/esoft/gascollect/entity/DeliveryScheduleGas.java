package com.esoft.gascollect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryScheduleGas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Primary Key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_schedule_id", nullable = false)
    private DeliverySchedule deliverySchedule; // Link to DeliverySchedule

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gas_id", nullable = false)
    private Gas gas; // Gas type

    private int quantity; // Quantity of this gas type being delivered
}
