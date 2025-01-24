package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GasDTO {
    private int id;

    private int capacity;

    private int price;

    private int stock;
}
