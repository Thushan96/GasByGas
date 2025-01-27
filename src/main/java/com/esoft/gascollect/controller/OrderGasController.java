package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.OrderGasDTO;
import com.esoft.gascollect.service.OrderGasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/ordergas")
public class OrderGasController {

    private final OrderGasService orderGasService;

    @Autowired
    public OrderGasController(OrderGasService orderGasService) {
        this.orderGasService = orderGasService;
    }

    @PostMapping
    public ResponseEntity<OrderGasDTO> createOrderGas(@RequestBody OrderGasDTO orderGasDTO) {
        OrderGasDTO createdOrderGas = orderGasService.createOrderGas(orderGasDTO);
        return ResponseEntity.ok(createdOrderGas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderGasDTO> getOrderGasById(@PathVariable int id) {
        OrderGasDTO orderGas = orderGasService.getOrderGasById(id);
        return ResponseEntity.ok(orderGas);
    }

    @GetMapping
    public ResponseEntity<List<OrderGasDTO>> getAllOrderGases() {
        List<OrderGasDTO> orderGases = orderGasService.getAllOrderGases();
        return ResponseEntity.ok(orderGases);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderGasDTO> updateOrderGas(@PathVariable int id, @RequestBody OrderGasDTO orderGasDTO) {
        OrderGasDTO updatedOrderGas = orderGasService.updateOrderGas(id, orderGasDTO);
        return ResponseEntity.ok(updatedOrderGas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderGas(@PathVariable int id) {
        orderGasService.deleteOrderGas(id);
        return ResponseEntity.noContent().build();
    }
}
