package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompletionDTO {
    private int deliveryScheduleId; // ID of the delivery schedule
    private List<DeliveryGasDTO> deliveredGases; // Details of delivered gases
}

