package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.GasDTO;
import com.esoft.gascollect.dto.NotificationDTO;
import com.esoft.gascollect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public void createNotifications(@RequestBody NotificationDTO notificationDTO){
        notificationService.createNotifications(notificationDTO);
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotifications(@PathVariable Integer id) {
        notificationService.deleteNotifications(id);
        return ResponseEntity.noContent().build();
    }
}
