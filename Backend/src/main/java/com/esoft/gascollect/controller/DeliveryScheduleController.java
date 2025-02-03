package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.DeliveryCompletionDTO;
import com.esoft.gascollect.service.DeliveryScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/api/delieveryShedule")
public class DeliveryScheduleController {

    private final DeliveryScheduleService deliveryScheduleService;

    public DeliveryScheduleController(DeliveryScheduleService deliveryScheduleService, DeliveryScheduleService deliveryScheduleService1) {
        this.deliveryScheduleService = deliveryScheduleService1;
    }

    @PostMapping
    public ResponseEntity addDeliverySchedule(@RequestBody DeliveryCompletionDTO deliveryCompletionDTO) {
        return deliveryScheduleService.completeDeliverySchedule(deliveryCompletionDTO);
    }

    @GetMapping
    public void sendNotificationToCustomer(){
         deliveryScheduleService.sendNotificationToCustomer1();
    }
}
