package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutletOrderDTO {
    private int outletId;
    private String outletName;
    private String outletLocation;
    private List<OrderDTO> orders; // List of OrderDTO objects for this outlet
}
