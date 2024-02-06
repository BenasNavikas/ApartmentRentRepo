package com.ar.apartmentrent.services;

import com.ar.apartmentrent.model.Admin;

public interface AdminService {
    Admin getAdminByUsername(String username);
}
