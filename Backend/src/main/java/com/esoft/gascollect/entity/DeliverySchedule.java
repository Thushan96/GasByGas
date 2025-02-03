package com.esoft.gascollect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outlet_id", nullable = false)
    private Outlet outlet;

    private String status;
    private String nextDeliveryDate;
    private boolean delivered;

    @OneToMany(mappedBy = "deliverySchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeliveryScheduleGas> deliveryScheduleGases;

    @Override
    public String toString() {
        return "DeliverySchedule{" +
                "id=" + id +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", status='" + status + '\'' +
                ", nextDeliveryDate='" + nextDeliveryDate + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}
