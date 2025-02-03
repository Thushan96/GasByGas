package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryGasDTO {
    private int gasId; // ID of the gas type
    private int quantity; // Quantity of gas delivered
}
