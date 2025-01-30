package com.esoft.gascollect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String name;
    private String contactNumber;
    private String email;
    private LocalDate preferredDate;
    private Integer gasCapacity;
}
