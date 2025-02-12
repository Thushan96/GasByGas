package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySheduleDTO {
    private int id;
    private String deliveryDate;
    private String status;
    private int outletId;
    private int qty;
    private List<DeliveryGasDTO> deliveryGasDTOS;
}
