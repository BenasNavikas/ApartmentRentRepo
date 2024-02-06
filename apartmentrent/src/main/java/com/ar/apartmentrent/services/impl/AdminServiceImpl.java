package com.ar.apartmentrent.services.impl;

import com.ar.apartmentrent.model.Admin;
import com.ar.apartmentrent.repository.AdminRepository;
import com.ar.apartmentrent.services.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public Admin getAdminByUsername(String username) {
        Optional<Admin> optionalAdmin = adminRepository.findByUserUsername(username);

        return optionalAdmin.orElse(null);
    }
}