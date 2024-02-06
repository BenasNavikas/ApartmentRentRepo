package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.RentCaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentCaseStatusRepository extends JpaRepository<RentCaseStatus, Integer> {
}
