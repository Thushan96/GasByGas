package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.DeliverySchedule;
import com.esoft.gascollect.entity.DeliveryScheduleGas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DelieverySheduleRepository extends JpaRepository<DeliverySchedule,Integer> {
    List<DeliverySchedule> findAllByOrderByIdDesc();

}
