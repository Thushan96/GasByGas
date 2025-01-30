package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.OrderSummaryDTO;
import com.esoft.gascollect.service.OrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orderSummary")
public class OrderSummaryController {

    @Autowired
    private OrderSummaryService orderSummaryService;

    @PostMapping
    public ResponseEntity<OrderSummaryDTO> createOrder(@RequestBody OrderSummaryDTO orderSummaryDTO) {
        OrderSummaryDTO createdOrder = orderSummaryService.createOrder(orderSummaryDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrders() {
        List<OrderSummaryDTO> orders = orderSummaryService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderSummaryDTO> getOrderById(@PathVariable Long id) {
        OrderSummaryDTO order = orderSummaryService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderSummaryDTO>> searchOrders(@RequestParam String customerName) {
        List<OrderSummaryDTO> orders = orderSummaryService.searchByCustomerName(customerName);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderSummaryService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}