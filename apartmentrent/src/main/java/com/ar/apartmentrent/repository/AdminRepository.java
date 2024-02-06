package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUserUsername(String username);
}
