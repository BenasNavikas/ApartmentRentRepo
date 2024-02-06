package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LesseeRepository extends JpaRepository<Lessee, Integer> {
//    Lessee findByLesseeidAndLessor_Lessorid(int lesseeid, int lessorid);

    Optional<Lessee> findByUser_Username(String username);
}
