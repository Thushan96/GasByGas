package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.DeliveryCompletionDTO;
import com.esoft.gascollect.dto.DeliveryGasDTO;
import com.esoft.gascollect.dto.OrderDTO;
import com.esoft.gascollect.entity.*;
import com.esoft.gascollect.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryScheduleService {

    private final DelieverySheduleRepository deliveryScheduleRepository;
    private final OutletRepository outletRepository;
    private final GasRepository gasRepository;
    private final DeliveryGasScheduleRepository deliveryGasScheduleRepository;
    private final OrderRepository orderRepository;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public DeliveryScheduleService(DelieverySheduleRepository deliveryScheduleRepository, OutletRepository outletRepository, GasRepository gasRepository, DeliveryGasScheduleRepository deliveryGasScheduleRepository, OrderRepository orderRepository, HttpServletResponse httpServletResponse) {
        this.deliveryScheduleRepository = deliveryScheduleRepository;
        this.outletRepository = outletRepository;
        this.gasRepository = gasRepository;
        this.deliveryGasScheduleRepository = deliveryGasScheduleRepository;
        this.orderRepository = orderRepository;
        this.httpServletResponse = httpServletResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity completeDeliverySchedule(DeliveryCompletionDTO deliveryCompletionDTO) {
        // Fetch the DeliverySchedule
        DeliverySchedule deliverySchedule = deliveryScheduleRepository.findById(deliveryCompletionDTO.getDeliveryScheduleId())
                .orElseThrow(() -> new RuntimeException("DeliverySchedule not found with id: " + deliveryCompletionDTO.getDeliveryScheduleId()));

        if (deliverySchedule.isDelivered()) {
            throw new RuntimeException("DeliverySchedule is already marked as delivered.");
        }

        // Fetch the Outlet in a managed state
        Outlet outlet = outletRepository.findById(deliverySchedule.getOutlet().getId())
                .orElseThrow(() -> new RuntimeException("Outlet not found for DeliverySchedule."));

        // Update the Outlet stock
        for (DeliveryGasDTO deliveredGas : deliveryCompletionDTO.getDeliveredGases()) {
            Gas gas = gasRepository.findById(deliveredGas.getGasId())
                    .orElseThrow(() -> new RuntimeException("Gas not found with id: " + deliveredGas.getGasId()));

            // Check if the gas exists in stock and update it
            Optional<OutletStock> existingStock = outlet.getStock().stream()
                    .filter(stock -> stock.getGasId() == gas.getId())
                    .findFirst();

            if (existingStock.isPresent()) {
                OutletStock stock = existingStock.get();
                stock.setQuantity(stock.getQuantity() + deliveredGas.getQuantity());
            } else {
                // Add new stock entry if gas is not found
                OutletStock newStock = new OutletStock(gas.getId(), deliveredGas.getQuantity());
                outlet.getStock().add(newStock);
            }
        }

        // Save the updated outlet entity (ensures collection changes are persisted)
        outletRepository.save(outlet);

        // Mark the delivery schedule as delivered
        deliverySchedule.setDelivered(true);
        deliverySchedule.setStatus("Delivered");
        deliveryScheduleRepository.save(deliverySchedule);

        // Fetch related Orders
        List<OrderDTO> relatedOrders = findByDeliverySchedule(deliveryCompletionDTO.getDeliveryScheduleId());
        for (OrderDTO orderDTO : relatedOrders) {
            Order order = orderRepository.findById(orderDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderDTO.getId()));

            order.setStatus("Pending");
            orderRepository.save(order);

            // Notify the customer
            User user = order.getUser();
            sendNotificationToCustomer(user, deliverySchedule.getDeliveryDate());
        }
        return ResponseEntity.ok().build();
    }

    // Method to fetch orders by DeliverySchedule ID
    public List<OrderDTO> findByDeliverySchedule(int deliveryScheduleId) {
        // Fetch the DeliverySchedule
        DeliverySchedule deliverySchedule = deliveryScheduleRepository.findById(deliveryScheduleId)
                .orElseThrow(() -> new RuntimeException("DeliverySchedule not found with id: " + deliveryScheduleId));

        // Delegate to the overloaded method
        return findByDeliverySchedule(deliverySchedule);
    }

    // Overloaded method to fetch orders directly by DeliverySchedule entity
    public List<OrderDTO> findByDeliverySchedule(DeliverySchedule deliverySchedule) {
        // Use internal logic to fetch orders related to the delivery schedule
        List<Order> orders = findOrdersByDeliverySchedule(deliverySchedule);

        // Convert entities to DTOs
        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    BeanUtils.copyProperties(order, orderDTO);
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    // Internal method to fetch orders linked to a delivery schedule
    private List<Order> findOrdersByDeliverySchedule(DeliverySchedule deliverySchedule) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getDeliverySchedule() != null &&
                        order.getDeliverySchedule().getId() == deliverySchedule.getId())
                .toList();
    }


    private void sendNotificationToCustomer(User user, String deliveryDate) {
//        String subject = "Delivery Notification";
//        String message = "Dear " + user.getName() + ",\n\n" +
//                "Your delivery is scheduled for " + deliveryDate + ".\n\n" +
//                "Thank you for choosing our service!";
//        mailgunService.sendEmail("+94770347469", subject, message);
    }

    public void sendNotificationToCustomer1() {
//        String subject = "Delivery Notification";
//        String message = "Dear " + "bro" + ",\n\n" +
//                "Your delivery is scheduled for " + "yo yoooooo" + ".\n\n" +
//                "Thank you for choosing our service!";
//        twilioService.sendSms("+94770347469", message);
    }
}
