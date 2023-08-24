package com.madroid.repository;

import com.madroid.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StationRepository extends JpaRepository<Station, Long>, JpaSpecificationExecutor<Station> {
}
