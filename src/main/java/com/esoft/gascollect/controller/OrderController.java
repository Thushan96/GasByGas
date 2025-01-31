package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.OrderDTO;
import com.esoft.gascollect.dto.OrderRequestDTO;
import com.esoft.gascollect.dto.OutletOrderDTO;
import com.esoft.gascollect.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @PostMapping("/schedule")
    public ResponseEntity<OrderDTO> createSheduleOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrderWithDeliveryRequest(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @PostMapping("/schedule/order")
    public ResponseEntity<OrderDTO> sellRequestedOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO createdOrder = orderService.sellRequestedOrder(orderRequestDTO.getId());
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/last-id")
    public ResponseEntity<?> getLastOrderId() {
        Integer lastOrderId = orderService.getLastOrderId();

        if (lastOrderId != null) {
            return ResponseEntity.ok(lastOrderId);
        } else {
            return ResponseEntity.ok("No orders found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/requested")
    public ResponseEntity<List<OrderDTO>> getAllRequestedOrders() {
        List<OrderDTO> orders = orderService.getAllRequestedOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/requested/outlet")
    public ResponseEntity<List<OutletOrderDTO>> getAllRequestedOutletOrdersByOutlet() {
        List<OutletOrderDTO> orders = orderService.getOrdersByStatusGroupedByOutlet();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable int id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
