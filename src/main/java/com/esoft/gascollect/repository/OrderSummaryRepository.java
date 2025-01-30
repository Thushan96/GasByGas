package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long> {
    List<OrderSummary> findByUserId(Long userId);
    List<OrderSummary> findByCustomerNameContainingIgnoreCase(String customerName);
}