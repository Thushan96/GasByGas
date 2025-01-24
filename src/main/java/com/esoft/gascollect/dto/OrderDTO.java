package com.esoft.gascollect.dto;

import com.esoft.gascollect.dto.OrderGasDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private String status;     // e.g., "Pending", "Delivered", "Cancelled"
    private String tokenNumber; // Unique token for the order
//    private String pickupDate; // Date for pickup or delivery
    private int userId;
    private Integer deliveryScheduleId; // Nullable for immediate stock orders
    private Integer outletId; // Add this field for linking the order to an outlet
    private List<OrderGasDTO> orderGasList; // List of gas items with quantity and other details
}
