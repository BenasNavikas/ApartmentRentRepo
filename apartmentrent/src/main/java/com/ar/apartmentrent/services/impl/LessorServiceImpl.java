package com.ar.apartmentrent.services.impl;

import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.Role;
import com.ar.apartmentrent.model.User;
import com.ar.apartmentrent.model.dto.LessorDTO;
import com.ar.apartmentrent.repository.LesseeRepository;
import com.ar.apartmentrent.repository.LessorRepository;
import com.ar.apartmentrent.repository.UserRepository;
import com.ar.apartmentrent.services.LessorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessorServiceImpl implements LessorService {


    @Autowired
    LessorRepository lessorRepository;
    @Autowired
    private LesseeRepository lesseeRepository;
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Lessor findBylessorid(int id) {
        Optional<Lessor> optionalLessor = lessorRepository.findById(id);

        return optionalLessor.orElseThrow(() -> new EntityNotFoundException("lessor not found with id " + id));
    }

    @Override
    public List<Lessor> getAllLessors() {
        return lessorRepository.findAll();
    }

    @Override
    public Lessor editByLessorId(LessorDTO lessorDTO, int id) {
        Optional<Lessor> optionalLessor = lessorRepository.findById(id);
        if (optionalLessor.isPresent()) {
            Lessor lessor = optionalLessor.get();
            if (Objects.nonNull(lessorDTO.getName())) {
                lessor.setName(lessorDTO.getName());
            }
            if (Objects.nonNull(lessorDTO.getAddress())) {
                lessor.setAddress(lessorDTO.getAddress());
            }
            if (Objects.nonNull(lessorDTO.getPhoneNumber())) {
                lessor.setPhoneNumber(lessorDTO.getPhoneNumber());
            }
            if (Objects.nonNull(lessorDTO.getEmail())) {
                lessor.setEmail(lessorDTO.getEmail());
            }
            if (Objects.nonNull(lessorDTO.getAccountNumber())) {
                lessor.setAccountNumber(lessorDTO.getAccountNumber());
            }

            return lessorRepository.save(lessor);
        }
        throw new EntityNotFoundException(" not found with id " + id);
    }


    @Override
    public Lessor createLessor(LessorDTO lessorDTO) {
        Lessor lessor = new Lessor();
        lessor.setName(lessorDTO.getName());
        lessor.setAddress(lessorDTO.getAddress());
        lessor.setPhoneNumber(lessorDTO.getPhoneNumber());
        lessor.setEmail(lessorDTO.getEmail());
        lessor.setAccountNumber(lessorDTO.getAccountNumber());

        User customUser = new User();
        customUser.setUsername(!lessorDTO.getUsername().isEmpty() ? lessorDTO.getUsername() : lessor.getName());
        customUser.setPassword(passwordEncoder.encode("asdf"));
        customUser.setRole(Role.LESSOR);
        userRepository.save(customUser);

        lessor.setUser(customUser);


        return lessorRepository.save(lessor);
    }

    @Override
    public boolean deleteLessorByLessorId(int id) {
        Optional<Lessor> optionalLessor = lessorRepository.findById(id);
        if (optionalLessor.isPresent()) {


//            List<Lessee> lessees = lesseeRepository.findAll()
//                    .stream()
//                    .filter(lessee -> lessee.getLessor().getLessorid() == lessorid).toList();
//
//            if (lessees.isEmpty()) {
//                return false;
//            }
            try{
                lessorRepository.deleteById(id);
            }
            catch (Exception exception){
                return false;
            }

            return true;
        }
        throw new EntityNotFoundException("Lessor not found with id " + id);
    }

    @Override
    public Lessor getLessorByUsername(String username) {
        Optional<Lessor> optionalLessor = lessorRepository.findByUser_Username(username);
        return optionalLessor.orElseThrow(() -> new EntityNotFoundException("Creditor not found with username " + username));
    }
    @Override
    public Lessor getLessorByUsername2(String username) {
        Optional<Lessor> optionalLessor = lessorRepository.findByUser_Username(username);
        return optionalLessor.orElse(null);
    }
}
