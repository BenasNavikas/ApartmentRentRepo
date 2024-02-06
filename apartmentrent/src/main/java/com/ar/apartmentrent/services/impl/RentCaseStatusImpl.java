package com.ar.apartmentrent.services.impl;

import com.ar.apartmentrent.model.RentCaseStatus;
import com.ar.apartmentrent.repository.RentCaseStatusRepository;
import com.ar.apartmentrent.services.RentCaseStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RentCaseStatusImpl implements RentCaseStatusService {
    private final RentCaseStatusRepository rentCaseStatusRepository;

    @Override
    public RentCaseStatus getRentCaseStatusById(int id) {
        return rentCaseStatusRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rentcase status not found with id " + id));

    }
}
