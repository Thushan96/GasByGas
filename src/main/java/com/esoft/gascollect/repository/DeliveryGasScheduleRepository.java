package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.DeliveryScheduleGas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryGasScheduleRepository  extends JpaRepository<DeliveryScheduleGas,Integer> {
}
