package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.DeliveryCompletionDTO;
import com.esoft.gascollect.dto.DeliverySheduleDTO;
import com.esoft.gascollect.service.DeliveryScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/delieveryShedule")
public class DeliveryScheduleController {

    private final DeliveryScheduleService deliveryScheduleService;

    public DeliveryScheduleController(DeliveryScheduleService deliveryScheduleService, DeliveryScheduleService deliveryScheduleService1) {
        this.deliveryScheduleService = deliveryScheduleService1;
    }

    @PostMapping
    public ResponseEntity<DeliveryCompletionDTO> addDeliverySchedule(@RequestBody DeliveryCompletionDTO deliveryCompletionDTO) {
        System.out.println("addDeliverySchedule ------------------------------"+deliveryCompletionDTO);
        return deliveryScheduleService.completeDeliverySchedule(deliveryCompletionDTO);
    }

    @GetMapping()
    public List<DeliverySheduleDTO> getDeliverySchedules() {
        return deliveryScheduleService.getfilteredDeliverySchedule();
    }
//
//    @GetMapping
//    public void sendNotificationToCustomer(){
//         deliveryScheduleService.sendNotificationToCustomer1();
//    }
}
