package com.ar.apartmentrent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessees")

public class Lessee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "address")
    String address;
    @Column(name = "phoneNumber")
    String phoneNumber;
    @Column(name = "email")
    String email;
    @Column(name = "accountNumber")
    String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lessor_id", nullable = false)
    private Lessor lessor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", unique = true)
    User user;
}
