package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.OrderGas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderGasRepository extends JpaRepository<OrderGas, Integer> {
}
