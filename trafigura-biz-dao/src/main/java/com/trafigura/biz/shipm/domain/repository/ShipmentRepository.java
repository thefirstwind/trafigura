package com.trafigura.biz.shipm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.trafigura.biz.shipm.domain.entity.Shipment;
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
	
	@Query("SELECT max(s.id) FROM Shipment s")
	public Long getLastId();

}