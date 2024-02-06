package com.ar.apartmentrent.services.impl;

import com.ar.apartmentrent.exception.ResourceNotFoundException;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.Role;
import com.ar.apartmentrent.model.User;
import com.ar.apartmentrent.model.dto.LesseeDTO;
import com.ar.apartmentrent.model.dto.LessorDTO;
import com.ar.apartmentrent.repository.LesseeRepository;
import com.ar.apartmentrent.repository.LessorRepository;
import com.ar.apartmentrent.repository.UserRepository;
import com.ar.apartmentrent.services.LesseeService;
import com.ar.apartmentrent.services.LessorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LesseeServiceImpl implements LesseeService {

    @Autowired
    LesseeRepository lesseeRepository;

    @Autowired
    LessorRepository lessorRepository;
    @Autowired
    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    @Override
//    public Lessee findByLesseeidAndByLessorid(int lesseeId, int lessorid){
//        Lessee lessee = lesseeRepository.findByLesseeidAndLessor_Lessorid(lesseeId, lessorid);
//        return lessee;
//    }

    @Override
    public Lessee findBylesseeid(int id) {
        Optional<Lessee> optionalLessee = lesseeRepository.findById(id);

        return optionalLessee.orElseThrow(() -> new EntityNotFoundException("lessee not found with id " + id));
    }

    @Override
    public List<Lessee> getAllLessees() {
        return lesseeRepository.findAll();
    }

//    @Override
//    public Lessee editByLesseeId(LesseeDTO lesseeDTO, int lesseeid, int lessorid) {
//        Optional<Lessee> optionalLessee = lesseeRepository.findById(lesseeid);
//        if (optionalLessee.isPresent()) {
//            Lessee lessee = optionalLessee.get();
//            if (Objects.nonNull(lesseeDTO.getName())) {
//                lessee.setName(lesseeDTO.getName());
//            }
//            if (Objects.nonNull(lesseeDTO.getAddress())) {
//                lessee.setAddress(lesseeDTO.getAddress());
//            }
//            if (Objects.nonNull(lesseeDTO.getPhoneNumber())) {
//                lessee.setPhoneNumber(lesseeDTO.getPhoneNumber());
//            }
//            if (Objects.nonNull(lesseeDTO.getEmail())) {
//                lessee.setEmail(lesseeDTO.getEmail());
//            }
//            if (Objects.nonNull(lesseeDTO.getAccountNumber())) {
//                lessee.setAccountNumber(lesseeDTO.getAccountNumber());
//            }
//            if (lessorid > 0){
//                lessee.setLessor(lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + lessorid)));
//            }
//
//            return lesseeRepository.save(lessee);
//        }
//        throw new EntityNotFoundException(" not found with id " + lesseeid);
//    }

    @Override
    public Lessee editByLesseeId(LesseeDTO lesseeDTO, int id) {
        Optional<Lessee> optionalLessee = lesseeRepository.findById(id);
        if (optionalLessee.isPresent()) {
            Lessee lessee = optionalLessee.get();
            if (Objects.nonNull(lesseeDTO.getName())) {
                lessee.setName(lesseeDTO.getName());
            }
            if (Objects.nonNull(lesseeDTO.getAddress())) {
                lessee.setAddress(lesseeDTO.getAddress());
            }
            if (Objects.nonNull(lesseeDTO.getPhoneNumber())) {
                lessee.setPhoneNumber(lesseeDTO.getPhoneNumber());
            }
            if (Objects.nonNull(lesseeDTO.getEmail())) {
                lessee.setEmail(lesseeDTO.getEmail());
            }
            if (Objects.nonNull(lesseeDTO.getAccountNumber())) {
                lessee.setAccountNumber(lesseeDTO.getAccountNumber());
            }
//            if (lessorid > 0){
//                lessee.setLessor(lessorRepository.findById(lessorid).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + lessorid)));
//            }

            return lesseeRepository.save(lessee);
        }
        throw new EntityNotFoundException(" not found with id " + id);
    }

    @Override
    public Lessee createLessee(LesseeDTO lesseeDTO, int id) {
        Lessee lessee = new Lessee();
        lessee.setName(lesseeDTO.getName());
        lessee.setAddress(lesseeDTO.getAddress());
        lessee.setPhoneNumber(lesseeDTO.getPhoneNumber());
        lessee.setEmail(lesseeDTO.getEmail());
        lessee.setAccountNumber(lesseeDTO.getAccountNumber());
        Lessor lessor = lessorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lessor not found with id: " + id));
        lessee.setLessor(lessor);

        User customUser = new User();
        customUser.setUsername(!lesseeDTO.getUsername().isEmpty() ? lesseeDTO.getUsername() : lessor.getName());
        customUser.setPassword(passwordEncoder.encode("asdf"));
        customUser.setRole(Role.LESSEE);
        userRepository.save(customUser);

        lessee.setUser(customUser);


        return lesseeRepository.save(lessee);
    }

    @Override
    public boolean deleteLesseeByLesseeId(int id) {
        Optional<Lessee> optionalLessee = lesseeRepository.findById(id);
        if (optionalLessee.isPresent()) {
            try{
                lesseeRepository.deleteById(id);
            }
            catch (Exception exception){
                return false;
            }

            return true;
        }
        throw new EntityNotFoundException("Lessee not found with id " + id);
    }

    @Override
    public Lessee getLesseeByUsername(String username) {
        Optional<Lessee> optionalLessee = lesseeRepository.findByUser_Username(username);
        return optionalLessee.orElseThrow(() -> new EntityNotFoundException("Lessee not found with username " + username));
    }
    @Override
    public Lessee getLesseeByUsername2(String username) {
        Optional<Lessee> optionalLessee = lesseeRepository.findByUser_Username(username);
        return optionalLessee.orElse(null);
    }
}
