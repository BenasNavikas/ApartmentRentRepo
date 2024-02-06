package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.RentCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RentCaseRepository extends JpaRepository<RentCase, Integer> {

    Optional<RentCase> findByRentAmountAndDueDateAndLessor_User_UsernameAndLessee_Name(BigDecimal amountOwed, LocalDateTime dueDate, String username, String name);
}
