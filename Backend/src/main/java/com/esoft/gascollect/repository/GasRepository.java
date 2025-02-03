package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.Gas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GasRepository extends JpaRepository<Gas, Integer> {
}
