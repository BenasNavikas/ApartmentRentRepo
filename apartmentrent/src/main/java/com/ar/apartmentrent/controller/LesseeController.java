package com.ar.apartmentrent.controller;

import com.ar.apartmentrent.exception.RequestBodyFieldValidationException;
import com.ar.apartmentrent.exception.ResourceNotFoundException;
import com.ar.apartmentrent.model.FieldValidationErrorModel;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.dto.LesseeDTO;
import com.ar.apartmentrent.repository.LesseeRepository;
import com.ar.apartmentrent.repository.LessorRepository;
import com.ar.apartmentrent.services.LesseeService;
import com.ar.apartmentrent.services.LessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api/v1/lessors/{lessorid}/lessees")
@RequestMapping("/api/v1/lessees")
@SecurityRequirement(name = "arapi")
public class LesseeController {

    @Autowired
    private LesseeRepository lesseeRepository;

    @Autowired
    private LessorRepository lessorRepository;

    @Autowired
    private LesseeService lesseeService;

    @GetMapping
    public List<Lessee> getAllLessees() {
        return lesseeRepository.findAll();
    }

//    @GetMapping("{lesseeid}")
//    public ResponseEntity<Lessee> getLesseeById(@Valid
//                                                    @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                    @PathVariable int lesseeid,
//                                                @Valid
//                                                    @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
//                                                    @PathVariable int lessorid) {
//        Lessee lessee = lesseeRepository.findByLesseeidAndLessor_Lessorid(lesseeid, lessorid);//orElseThrow(() -> new ResourceNotFoundException("Lesse not fount with id"));
//        return ResponseEntity.ok(lessee);
////        Lessee lessee = lesseeRepository.findById(lesseeid)
////                .orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
//        //return ResponseEntity.ok(lessee);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Lessee> getLesseeById2(@Valid
                                                @Min (value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                @PathVariable int id){
        Lessee lessee = null;

        lessee = lesseeService.findBylesseeid(id);

        return ResponseEntity.ok(lessee);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Lessee> createLessee(@Valid
                                                   @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                   @PathVariable int id, @RequestBody @Valid LesseeDTO lesseeDTO, BindingResult bindingResult) {
        validateRequestBodyFields(bindingResult);

        Lessee lessee = lesseeService.createLessee(lesseeDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessee);



//                Lessee lesseeEntity = new Lessee();
//        lesseeEntity.setName(lesseeDTO.getName());
//        lesseeEntity.setAddress(lesseeDTO.getAddress());
//        lesseeEntity.setEmail(lesseeDTO.getEmail());
//        lesseeEntity.setPhoneNumber(lesseeDTO.getPhoneNumber());
//        lesseeEntity.setAccountNumber(lesseeDTO.getAccountNumber());
//        Lessor lessor = lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + lessorid));
//        lesseeEntity.setLessor(lessor);
//        Lessee createdLessee = lesseeRepository.save(lesseeEntity);
//        return new ResponseEntity<>(createdLessee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lessee> updateLessee(@Valid
                                                   @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                @PathVariable int id,
                                                @RequestBody @Valid LesseeDTO lesseeDTO, BindingResult bindingResult) {
        validateRequestBodyFields(bindingResult);

        Lessee lessor = lesseeService.editByLesseeId(lesseeDTO, id);
        return ResponseEntity.ok(lessor);

//        Lessee existingLessee = lesseeRepository.findById(lesseeid)
//                .orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
//
//        existingLessee.setName(lesseeDetails.getName());
//        existingLessee.setAddress(lesseeDetails.getAddress());
//        existingLessee.setEmail(lesseeDetails.getEmail());
//        existingLessee.setPhoneNumber(lesseeDetails.getPhoneNumber());
//        existingLessee.setAccountNumber(lesseeDetails.getAccountNumber());
//
//        Lessee updatedLessee = lesseeRepository.save(existingLessee);
//
//        return ResponseEntity.ok(updatedLessee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLessee(@Valid
                                                   @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                   @PathVariable int id) {

        if(Boolean.TRUE.equals(lesseeService.deleteLesseeByLesseeId(id))){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body("Remove rent cases from lessee");

        //        Lessee lessee = lesseeRepository.findById(lesseeid)
//                .orElseThrow(() -> new ResourceNotFoundException("Lessee not found with id: " + lesseeid));
//
//        lesseeRepository.delete(lessee);
//
//        return ResponseEntity.ok("Lessee with ID " + lesseeid + " deleted successfully.");
    }

    private void validateRequestBodyFields(BindingResult bindingResult) {
        List<FieldValidationErrorModel> fieldValidationErrors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldValidationErrors.add(new FieldValidationErrorModel(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        if (!fieldValidationErrors.isEmpty()){
            throw new RequestBodyFieldValidationException(fieldValidationErrors);
        }
    }
}
