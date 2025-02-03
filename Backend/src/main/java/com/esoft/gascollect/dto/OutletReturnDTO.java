package com.esoft.gascollect.dto;

import com.esoft.gascollect.entity.DeliverySchedule;
import com.esoft.gascollect.entity.Order;
import com.esoft.gascollect.entity.OutletStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutletReturnDTO {
    private int id;

    private String location;
    private String name;
    private String contactDetails;
    private List<OutletStock> stock;
}
