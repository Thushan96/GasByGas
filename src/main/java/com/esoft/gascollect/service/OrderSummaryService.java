package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.OrderSummaryDTO;
import com.esoft.gascollect.entity.OrderSummary;
import com.esoft.gascollect.repository.OrderSummaryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderSummaryService {

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    public OrderSummaryDTO createOrder(OrderSummaryDTO orderSummaryDTO) {
        OrderSummary orderSummary = new OrderSummary();
        BeanUtils.copyProperties(orderSummaryDTO, orderSummary);
        OrderSummary savedOrder = orderSummaryRepository.save(orderSummary);
        BeanUtils.copyProperties(savedOrder, orderSummaryDTO);
        return orderSummaryDTO;
    }

    public List<OrderSummaryDTO> searchByUserId(Long userId) {
        List<OrderSummary> orders = orderSummaryRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> {
                    OrderSummaryDTO dto = new OrderSummaryDTO();
                    BeanUtils.copyProperties(order, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<OrderSummaryDTO> getAllOrders() {
        List<OrderSummary> orders = orderSummaryRepository.findAll();
        return orders.stream()
                .map(order -> {
                    OrderSummaryDTO dto = new OrderSummaryDTO();
                    BeanUtils.copyProperties(order, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public OrderSummaryDTO getOrderById(Long id) {
        OrderSummary order = orderSummaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        OrderSummaryDTO dto = new OrderSummaryDTO();
        BeanUtils.copyProperties(order, dto);
        return dto;
    }

    public List<OrderSummaryDTO> searchByCustomerName(String customerName) {
        List<OrderSummary> orders = orderSummaryRepository.findByCustomerNameContainingIgnoreCase(customerName);
        return orders.stream()
                .map(order -> {
                    OrderSummaryDTO dto = new OrderSummaryDTO();
                    BeanUtils.copyProperties(order, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void deleteOrder(Long id) {
        orderSummaryRepository.deleteById(id);
    }
}