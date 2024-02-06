package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.Lessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LessorRepository extends JpaRepository<Lessor, Integer> {
    // all crud database methods
    //Optional<Lessor> findByUserUsername(String username);

    Optional<Lessor> findByUser_Username(String username);
}


