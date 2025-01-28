package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findAllByStatus(@Param("status") String status);

    @Query("SELECT o.outlet, o FROM Order o WHERE o.status = :status")
    List<Object[]> findOrdersByStatusGroupedByOutlet(@Param("status") String status);

}
