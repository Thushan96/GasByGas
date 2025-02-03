package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.OrderGasDTO;
import com.esoft.gascollect.entity.OrderGas;


import com.esoft.gascollect.repository.OrderGasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderGasService {

    private final OrderGasRepository orderGasRepository;

    @Autowired
    public OrderGasService(OrderGasRepository orderGasRepository) {
        this.orderGasRepository = orderGasRepository;
    }

    public OrderGasDTO createOrderGas(OrderGasDTO orderGasDTO) {
        OrderGas orderGas = new OrderGas();
        BeanUtils.copyProperties(orderGasDTO, orderGas);
        OrderGas savedOrderGas = orderGasRepository.save(orderGas);

        OrderGasDTO savedOrderGasDTO = new OrderGasDTO();
        BeanUtils.copyProperties(savedOrderGas, savedOrderGasDTO);
        return savedOrderGasDTO;
    }

    public OrderGasDTO getOrderGasById(int id) {
        OrderGas orderGas = orderGasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGas not found with id: " + id));
        OrderGasDTO orderGasDTO = new OrderGasDTO();
        BeanUtils.copyProperties(orderGas, orderGasDTO);
        return orderGasDTO;
    }

    public List<OrderGasDTO> getAllOrderGases() {
        return orderGasRepository.findAll().stream().map(orderGas -> {
            OrderGasDTO orderGasDTO = new OrderGasDTO();
            BeanUtils.copyProperties(orderGas, orderGasDTO);
            return orderGasDTO;
        }).collect(Collectors.toList());
    }

    public OrderGasDTO updateOrderGas(int id, OrderGasDTO orderGasDTO) {
        // Find the existing OrderGas by ID
        OrderGas orderGas = orderGasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGas not found with id: " + id));

//        if (orderGasDTO.getGasId() != 0) {
//            orderGas.setId(orderGasDTO.getGasId());
//        }
        if (orderGasDTO.getQuantity() != 0) {
            orderGas.setQuantity(orderGasDTO.getQuantity());
        }

        // Save the updated OrderGas
        OrderGas updatedOrderGas = orderGasRepository.save(orderGas);

        // Convert the updated OrderGas to a DTO and return it
        OrderGasDTO updatedOrderGasDTO = new OrderGasDTO();
        updatedOrderGasDTO.setId(updatedOrderGas.getId());
        updatedOrderGasDTO.setGasId(updatedOrderGas.getId());
        updatedOrderGasDTO.setQuantity(updatedOrderGas.getQuantity());

        return updatedOrderGasDTO;
    }

    public void deleteOrderGas(int id) {
        OrderGas orderGas = orderGasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGas not found with id: " + id));
        orderGasRepository.delete(orderGas);
    }
}
