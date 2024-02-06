package com.ar.apartmentrent.controller;

import com.ar.apartmentrent.exception.RequestBodyFieldValidationException;
import com.ar.apartmentrent.exception.ResourceNotFoundException;
import com.ar.apartmentrent.model.FieldValidationErrorModel;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.dto.LessorDTO;
import com.ar.apartmentrent.repository.LessorRepository;
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

import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lessors")
@SecurityRequirement(name = "arapi")
public class LessorController {

    @Autowired
    private LessorRepository lessorRepository;
    @Autowired
    private LessorService lessorService;

//    @GetMapping
//    public List<Lessor> getAllLessors(){
//        return lessorRepository.findAll();
//    }

    @GetMapping
    public ResponseEntity<List<Lessor>> getAllLessors(){
        List<Lessor> lessors = lessorService.getAllLessors();
        return ResponseEntity.ok(lessors);
    }

    // build create lessor REST API
    @PostMapping
    public ResponseEntity<Lessor> createLessor(@RequestBody @Valid LessorDTO lessorDTO, BindingResult bindingResult) {
        validateRequestBodyFields(bindingResult);

        Lessor lessor = lessorService.createLessor(lessorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessor);


//        Lessor lessorEntity = new Lessor();
//        lessorEntity.setName(lessor.getName());
//        lessorEntity.setAddress(lessor.getAddress());
//        lessorEntity.setEmail(lessor.getEmail());
//        lessorEntity.setPhoneNumber(lessor.getPhoneNumber());
//        lessorEntity.setAccountNumber(lessor.getAccountNumber());

//        return new ResponseEntity<>(lessorRepository.save(lessorEntity), HttpStatus.CREATED);
    }

    // build get lessor by id REST API
    @GetMapping("{id}")
    public ResponseEntity<Lessor> getLessorById(@Valid
                                                @Min (value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                @PathVariable int id){
        Lessor lessor = null;

        //if(lessorid != null){
            lessor = lessorService.findBylessorid(id);
        //}
        //Lessor lessor = lessorService.findBylessorid(lessorid);
        return ResponseEntity.ok(lessor);
    }

    //build update lessor REST API
    @PutMapping("{id}")
    public ResponseEntity<Lessor> updateLessor(@Valid
                                                   @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                   @PathVariable int id,@RequestBody @Valid LessorDTO lessorDTO, BindingResult bindingResult){
        validateRequestBodyFields(bindingResult);

        Lessor lessor = lessorService.editByLessorId(lessorDTO, id);
        return ResponseEntity.ok(lessor);


//        Lessor updateLessor = lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not exist with id"));
//
//        updateLessor.setName(lessorDetails.getName());
//        updateLessor.setAddress(lessorDetails.getAddress());
//        updateLessor.setEmail(lessorDetails.getEmail());
//        updateLessor.setPhoneNumber(lessorDetails.getPhoneNumber());
//        updateLessor.setAccountNumber(lessorDetails.getAccountNumber());
//
//        lessorRepository.save(updateLessor);
//
//        return ResponseEntity.ok(updateLessor);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteLessor(@Valid @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                    @PathVariable int id){
        if(Boolean.TRUE.equals(lessorService.deleteLessorByLessorId(id))){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body("Remove lessees from lessor");



//        Lessor lessor = lessorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lessor not exist with id"));
//
//        lessorRepository.delete(lessor);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
