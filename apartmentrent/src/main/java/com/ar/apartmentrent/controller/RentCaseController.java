package com.ar.apartmentrent.controller;

import com.ar.apartmentrent.exception.RequestBodyFieldValidationException;
import com.ar.apartmentrent.exception.ResourceNotFoundException;
import com.ar.apartmentrent.model.FieldValidationErrorModel;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.RentCase;
import com.ar.apartmentrent.model.dto.RentCaseDTO;
import com.ar.apartmentrent.repository.LesseeRepository;
import com.ar.apartmentrent.repository.RentCaseRepository;
import com.ar.apartmentrent.services.RentCaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@CrossOrigin
@RequiredArgsConstructor
//@RequestMapping("/api/v1/lessees/{lesseeid}/rentCases")
@RequestMapping("/api/v1/rentcases")
@SecurityRequirement(name = "arapi")
public class RentCaseController {

//    @Autowired
//    private RentCaseRepository rentCaseRepository;
//    @Autowired
//    private RentCaseService rentCaseService;
//
//    @Autowired
//    private LesseeRepository lesseeRepository;
//
//    @GetMapping
//    public List<RentCase> getAllRentCases() { return rentCaseRepository.findAll(); }
//
//    @PostMapping
//    public ResponseEntity<RentCase> createRentCase(@Valid
//                                                       @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                       @PathVariable int lesseeid, @RequestBody @Valid RentCaseDTO rentCaseDTO, BindingResult bindingResult) {
//        validateRequestBodyFields(bindingResult);
//
//        RentCase rentCase = rentCaseService.createRentCase(lesseeid, rentCaseDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(rentCase);
//
////        RentCase rentCaseEntity = new RentCase();
////        rentCaseEntity.setRentName(rentCaseDTO.getRentName());
////        rentCaseEntity.setRentAmount(rentCaseDTO.getRentAmount());
////        rentCaseEntity.setDueDate(rentCaseDTO.getDueDate());
////
////        Lessee lessee = lesseeRepository.findById(lesseeid).orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
////        rentCaseEntity.setLessee(lessee);
////
////        return new ResponseEntity<>(rentCaseRepository.save(rentCaseEntity), HttpStatus.CREATED);
//    }
//
//    @GetMapping("{rentCaseId}")
//    public ResponseEntity<RentCase> getRentCaseById(@Valid
//                                                        @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                        @PathVariable int rentCaseId) {
//
//        RentCase rentCase = rentCaseService.findByRentCaseid(rentCaseId);
//        return ResponseEntity.ok(rentCase);
//
////        RentCase rentCase = rentCaseRepository.findById(rentCaseId)
////                .orElseThrow(() -> new ResourceNotFoundException("Rent case not found with id: " + rentCaseId));
////        return ResponseEntity.ok(rentCase);
//    }
//
//    @PutMapping("{rentCaseId}")
//    public ResponseEntity<RentCase> updateRentCase(@Valid
//                                                       @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                       @PathVariable int rentCaseId, @Valid
//                                                        @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                        @PathVariable int lesseeid, @RequestBody @Valid RentCaseDTO rentCaseDTO, BindingResult bindingResult) {
//        validateRequestBodyFields(bindingResult);
//
//        RentCase rentCase = rentCaseService.editByRentCaseid(rentCaseDTO, rentCaseId, lesseeid);
//        return ResponseEntity.ok(rentCase);
//
//
////        RentCase updateRentCase = rentCaseRepository.findById(rentCaseId)
////                .orElseThrow(() -> new ResourceNotFoundException("Rent case not found with id: " + rentCaseId));
////
////        updateRentCase.setRentName(rentCaseDTO.getRentName());
////        updateRentCase.setRentAmount(rentCaseDTO.getRentAmount());
////
////
////        rentCaseRepository.save(updateRentCase);
////
////        return ResponseEntity.ok(updateRentCase);
//    }
//
//    @DeleteMapping("{rentCaseId}")
//    public ResponseEntity<String> deleteRentCase(@Valid
//                                                     @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                     @PathVariable int rentCaseId) {
//
//        if(Boolean.TRUE.equals(rentCaseService.deleteRentCaseByRentCaseId(rentCaseId))){
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.badRequest().body("Something went wrong");
//
////        RentCase rentCase = rentCaseRepository.findById(rentCaseId)
////                .orElseThrow(() -> new ResourceNotFoundException("Rent case not found with id: " + rentCaseId));
////
////        rentCaseRepository.delete(rentCase);
////
////        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
    private void validateRequestBodyFields(BindingResult bindingResult) {
        List<FieldValidationErrorModel> fieldValidationErrors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldValidationErrors.add(new FieldValidationErrorModel(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        if (!fieldValidationErrors.isEmpty()) {
            throw new RequestBodyFieldValidationException(fieldValidationErrors);
        }
    }
    private final RentCaseService rentCaseService;

    @GetMapping()
    public ResponseEntity<List<RentCase>> getAllRentCases() {
        List<RentCase> rentCases = rentCaseService.getAllRentCases();
        return ResponseEntity.ok(rentCases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentCase> getRentCaseById(@Valid
                                                    @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                    @PathVariable(name = "id") int id) {
        RentCase rentCase = rentCaseService.getRentCaseById(id);
        return ResponseEntity.ok(rentCase);
    }

    @GetMapping("/lessor/{username}")
    public ResponseEntity<List<RentCase>> getRentCasesByLessorUsername(@Valid
                                                                         @NotBlank
                                                                         @PathVariable(name = "username") String username) {
        List<RentCase> rentCases = rentCaseService.getRentCasesByLessorUsername(username);
        return ResponseEntity.ok(rentCases);
    }

    @GetMapping("/lessee/{username}")
    public ResponseEntity<List<RentCase>> getRentCasesByLesseeUsername(@Valid
                                                                       @PathVariable(name = "username") String username) {
        List<RentCase> rentCases = rentCaseService.getRentCasesByLesseeUsername(username);
        return ResponseEntity.ok(rentCases);
    }

    @PostMapping("/{lessorid}/{lesseeid}")
    public ResponseEntity<RentCase> createRentCase(@Valid @RequestBody RentCaseDTO rentCaseDTO, BindingResult result,
                                                   @Valid
                                                    @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                    @PathVariable int lesseeid,
                                                @Valid
                                                    @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                    @PathVariable int lessorid) {
        validateRequestBodyFields(result);

        RentCase rentCase = rentCaseService.createRentCase(lessorid, lesseeid, rentCaseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentCase);

//        RentCase rentCaseEntity = new RentCase();
//        rentCaseEntity.setRentName(rentCaseDTO.getRentName());
//        rentCaseEntity.setRentAmount(rentCaseDTO.getRentAmount());
//        rentCaseEntity.setDueDate(rentCaseDTO.getDueDate());
//
//        Lessee lessee = lesseeRepository.findById(lesseeid).orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
//        rentCaseEntity.setLessee(lessee);
//
//        return new ResponseEntity<>(rentCaseRepository.save(rentCaseEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentCase> editRentCaseById(@Valid @RequestBody RentCaseDTO rentCaseDTO, BindingResult result,
                                                     @Valid
                                                     @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                     @PathVariable(name = "id") int id) {
        RentCase rentCase = rentCaseService.editRentCaseById(rentCaseDTO, id);
        return ResponseEntity.ok(rentCase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentCaseById(@Valid
                                                     @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                     @PathVariable(name = "id") int id) {
        if (Boolean.TRUE.equals(rentCaseService.deleteRentCaseById(id))) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
