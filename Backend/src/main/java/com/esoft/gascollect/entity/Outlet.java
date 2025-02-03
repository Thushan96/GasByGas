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
public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String location;
    private String name;
    private String contactDetails;

    @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeliverySchedule> deliverySchedules;

    @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ElementCollection
    @CollectionTable(name = "outlet_stock", joinColumns = @JoinColumn(name = "outlet_id"))
    @Column(name = "stock")
    private List<OutletStock> stock;

    @Override
    public String toString() {
        return "Outlet{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", contactDetails='" + contactDetails + '\'' +
                '}';
    }
}
