package com.ar.apartmentrent.services;

import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.dto.LessorDTO;

import java.util.List;
import java.util.Optional;

public interface LessorService {

    Lessor findBylessorid(int id);

    List<Lessor> getAllLessors();
    Lessor editByLessorId(LessorDTO lessorDTO, int id);
    Lessor createLessor(LessorDTO lessorDTO);
    Lessor getLessorByUsername(String username);
    Lessor getLessorByUsername2(String username);

    boolean deleteLessorByLessorId(int id);
}
