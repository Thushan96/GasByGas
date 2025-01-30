package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.GasDTO;
import com.esoft.gascollect.dto.NotificationDTO;
import com.esoft.gascollect.entity.Gas;
import com.esoft.gascollect.entity.Notification;
import com.esoft.gascollect.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public void createNotifications(NotificationDTO notificationDTO) {
        Notification notification = objectMapper.convertValue(notificationDTO, Notification.class);
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            return notificationDTO;
        }).collect(Collectors.toList());
    }

    public void deleteNotifications(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }
}
