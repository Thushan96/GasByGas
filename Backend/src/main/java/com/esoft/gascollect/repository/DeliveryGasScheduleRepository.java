package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.DeliveryScheduleGas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryGasScheduleRepository  extends JpaRepository<DeliveryScheduleGas,Integer> {
    List<DeliveryScheduleGas> findByDeliverySchedule_Id(Integer deliveryGasId);
}
