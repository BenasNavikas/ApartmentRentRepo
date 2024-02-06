package com.ar.apartmentrent.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessors")

public class Lessor {
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
    //@JsonIgnore
    String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", unique = true)
    User user;

//    @JsonManagedReference(value = "lessor")
//    @OneToMany(mappedBy = "lessor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    Set<Lessee> lessees;

}
