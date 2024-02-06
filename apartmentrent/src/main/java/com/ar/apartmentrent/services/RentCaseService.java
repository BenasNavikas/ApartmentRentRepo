package com.ar.apartmentrent.services;

import com.ar.apartmentrent.model.RentCase;
import com.ar.apartmentrent.model.dto.RentCaseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RentCaseService {
    //    Lessor findBylessorid(int lessorid)
//    RentCase findByRentCaseid(int rentCaseid);
//    List<Lessor> getAllLessors();
//    List<RentCase> getAllRentCases();
//    Lessor editByLessorId(LessorDTO lessorDTO, int lessorid);
//    RentCase editByRentCaseid(RentCaseDTO rentCaseDTO, int rentCaseid, int lesseeid);
//    Lessor createLessor(LessorDTO lessorDTO);
    public RentCase createRentCase(int lessorid, int lesseeid, RentCaseDTO rentCaseDTO);
//    boolean deleteLessorByLessorId(int lessorid);
//    boolean deleteRentCaseByRentCaseId(int rentCaseid);
    List<RentCase> getAllRentCases();

    RentCase getRentCaseById(int id);

    List<RentCase> getRentCasesByLessorUsername(String username);

    List<RentCase> getRentCasesByLesseeUsername(String username);

//    List<RentCase> createRentCase(MultipartFile file, String username) throws CsvValidationException, IOException, InvalidFileFormatException;

    RentCase editRentCaseById(RentCaseDTO rentCaseDTO, int id);

    boolean deleteRentCaseById(int id);

    void markRentCaseEmailAsSentById(int id);

    Optional<RentCase> findExistingRentCase(String username, String... indicator);
}
