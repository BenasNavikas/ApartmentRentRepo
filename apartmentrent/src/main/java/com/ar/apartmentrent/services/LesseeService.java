package com.ar.apartmentrent.services;

import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.dto.LesseeDTO;
import com.ar.apartmentrent.model.dto.LessorDTO;

import java.util.List;
import java.util.Optional;

public interface LesseeService {

//    Lessee findByLesseeidAndByLessorid(int lesseeId, int lessorid);

    public Lessee findBylesseeid(int id);

    List<Lessee> getAllLessees();
//    Lessee editByLesseeId(LesseeDTO lesseeDTO, int lesseeid, int lessorid);
    Lessee editByLesseeId(LesseeDTO lesseeDTO, int id);

    Lessee createLessee(LesseeDTO lesseeDTO, int id);

    Lessee getLesseeByUsername(String username);
    Lessee getLesseeByUsername2(String username);

    boolean deleteLesseeByLesseeId(int id);

}
