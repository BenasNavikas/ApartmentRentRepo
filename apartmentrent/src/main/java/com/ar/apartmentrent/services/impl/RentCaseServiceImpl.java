package com.ar.apartmentrent.services.impl;

import com.ar.apartmentrent.exception.ResourceNotFoundException;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.RentCase;
import com.ar.apartmentrent.model.RentCaseStatus;
import com.ar.apartmentrent.model.dto.RentCaseDTO;
import com.ar.apartmentrent.repository.*;
import com.ar.apartmentrent.services.RentCaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentCaseServiceImpl implements RentCaseService {
//    @Autowired
//    RentCaseRepository rentCaseRepository;
//    @Autowired
//    LesseeRepository lesseeRepository;
//
//
//    @Override
//    public RentCase findByRentCaseid(int rentCaseid) {
//        Optional<RentCase> optionalRentCase = rentCaseRepository.findById(rentCaseid);
//
//        return optionalRentCase.orElseThrow(() -> new EntityNotFoundException("lessor not found with id " + rentCaseid));
//    }
//
//    @Override
//    public List<RentCase> getAllRentCases() {
//        return rentCaseRepository.findAll();
//    }
//
//    @Override
//    public RentCase editByRentCaseid(RentCaseDTO rentCaseDTO, int rentCaseid, int lesseeid) {
//        Optional<RentCase> optionalRentCase = rentCaseRepository.findById(rentCaseid);
//        if (optionalRentCase.isPresent()) {
//            RentCase rentCase = optionalRentCase.get();
//            if (Objects.nonNull(rentCaseDTO.getRentName())) {
//                rentCase.setRentName(rentCaseDTO.getRentName());
//            }
//            if (Objects.nonNull(rentCaseDTO.getRentAmount())) {
//                rentCase.setRentAmount(rentCaseDTO.getRentAmount());
//            }
//            if (Objects.nonNull(rentCaseDTO.getDueDate())) {
//                rentCase.setDueDate(rentCaseDTO.getDueDate());
//            }
//
//            if (lesseeid > 0) {
//                rentCase.setLessee(lesseeRepository.findById(lesseeid).orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid)));
//            }
//
//            return rentCaseRepository.save(rentCase);
//        }
//        throw new EntityNotFoundException(" not found with id " + rentCaseid);
//    }
//
//    @Override
//    public RentCase createRentCase(int lesseeid, RentCaseDTO rentCaseDTO) {
////        Lessee lessee = new Lessee();
////        lessee.setName(lessee.getName());
////        lessee.setAddress(lesseeDTO.getAddress());
////        lessee.setPhoneNumber(lesseeDTO.getPhoneNumber());
////        lessee.setEmail(lesseeDTO.getEmail());
////        lessee.setAccountNumber(lesseeDTO.getAccountNumber());
////        Lessor lessor = lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + lessorid));
////        lessee.setLessor(lessor);
////
////
////        return lesseeRepository.save(lessee);
//
//        RentCase rentCaseEntity = new RentCase();
//        rentCaseEntity.setRentName(rentCaseDTO.getRentName());
//        rentCaseEntity.setRentAmount(rentCaseDTO.getRentAmount());
//        rentCaseEntity.setDueDate(rentCaseDTO.getDueDate());
//
//        Lessee lessee = lesseeRepository.findById(lesseeid).orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
//        rentCaseEntity.setLessee(lessee);
//
//        return rentCaseRepository.save(rentCaseEntity);
//    }
//
//    @Override
//    public boolean deleteRentCaseByRentCaseId(int rentCaseid) {
//            Optional<RentCase> optionalRentCase = rentCaseRepository.findById(rentCaseid);
//            if (optionalRentCase.isPresent()) {
//                try{
//                    lesseeRepository.deleteById(rentCaseid);
//                }
//                catch (Exception exception){
//                    return false;
//                }
//
//                return true;
//            }
//            throw new EntityNotFoundException("Rent Case not found with id " + rentCaseid);
//    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RentCaseRepository rentCaseRepository;
    private final RentCaseStatusRepository rentCaseStatusRepository;
    private final RentCaseStatusImpl rentCaseStatusService;
    private final LesseeRepository lesseeRepository;
    private final LessorRepository lessorRepository;
//    private final CsvService csvService;

    @Override
    public List<RentCase> getAllRentCases() {
        return rentCaseRepository.findAll();
    }

    @Override
    public RentCase getRentCaseById(int id) {
        Optional<RentCase> optionalRentCase = rentCaseRepository.findById(id);

        return optionalRentCase.orElseThrow(() -> new EntityNotFoundException("RentCase not found with id " + id));
    }

    @Override
    public List<RentCase> getRentCasesByLessorUsername(String username) {
        List<RentCase> rentCases = rentCaseRepository.findAll();

        return rentCases.stream()
                .filter(rentCase ->
                        Objects.equals(rentCase.getLessor().getUser().getUsername(), username))
                .toList();
    }

    @Override
    public List<RentCase> getRentCasesByLesseeUsername(String username) {
        List<RentCase> rentCases = rentCaseRepository.findAll();

        return rentCases.stream()
                .filter(rentCase -> Objects.equals(rentCase.getLessee().getUser().getUsername(), username))
                .toList();
    }

    @Override
    public RentCase createRentCase(int lessorid, int lesseeid, RentCaseDTO rentCaseDTO) {

        RentCase rentCaseEntity = new RentCase();
        rentCaseEntity.setRentAmount(rentCaseDTO.getRentAmount());
        rentCaseDTO.setDueDate(LocalDateTime.now());

        rentCaseEntity.setDueDate(rentCaseDTO.getDueDate());
        rentCaseEntity.setRentCaseStatus(rentCaseStatusService.getRentCaseStatusById(1));

        Lessor lessor = lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + lesseeid));
        rentCaseEntity.setLessor(lessor);

        Lessee lessee = lesseeRepository.findById(lesseeid).orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
        rentCaseEntity.setLessee(lessee);

        rentCaseEntity.setIsSent(0);

        return rentCaseRepository.save(rentCaseEntity);
    }

    @Override
    public RentCase editRentCaseById(RentCaseDTO rentCaseDTO, int id) {
        Optional<RentCase> optionalRentCase = rentCaseRepository.findById(id);
        if (optionalRentCase.isPresent()) {
            RentCase rentCase = optionalRentCase.get();
            if (Objects.nonNull(rentCaseDTO.getRentAmount())) {
                rentCase.setRentAmount(rentCaseDTO.getRentAmount());
            }
            if (Objects.nonNull(rentCaseDTO.getDueDate())) {
                rentCase.setDueDate(rentCaseDTO.getDueDate());
            }
            return rentCaseRepository.save(rentCase);
        }

        throw new EntityNotFoundException("RentCase not found with id " + id);
    }

    @Override
    public boolean deleteRentCaseById(int id) {
        Optional<RentCase> optionalRentCase = rentCaseRepository.findById(id);
        if (optionalRentCase.isPresent()) {
            rentCaseRepository.deleteById(id);

            return true;
        }

        throw new EntityNotFoundException("RentCase not found with id " + id);
    }

    @Override
    public void markRentCaseEmailAsSentById(int id) {
        RentCase rentCase = rentCaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RentCase not found with id " + id));

        rentCase.setIsSent(1);
        rentCaseRepository.save(rentCase);
    }

    @Override
    public Optional<RentCase> findExistingRentCase(String username, String... indicator) {
        return rentCaseRepository.findByRentAmountAndDueDateAndLessor_User_UsernameAndLessee_Name(
                new BigDecimal(indicator[0]),
                LocalDateTime.parse(indicator[1], DATE_TIME_FORMATTER),
                username,
                indicator[2]
        );
    }

    private String getTypeToMatch(String type) {
        return type.toUpperCase().contains("_RENT") ? type.toUpperCase() : type.toUpperCase().concat("_RENT");
    }
}
