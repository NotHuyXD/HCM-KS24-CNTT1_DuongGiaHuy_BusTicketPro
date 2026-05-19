package com.bus.repository;

import com.bus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByRoute_StartLocationAndRoute_EndLocation(String start, String end);
}