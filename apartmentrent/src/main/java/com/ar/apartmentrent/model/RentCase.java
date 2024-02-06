package com.ar.apartmentrent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentCases")

public class RentCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentCase_id")
    int rentCaseId;
    @Column(name = "rentAmount")
    BigDecimal rentAmount;
    @Column(name = "localDateTime")
    LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    RentCaseStatus rentCaseStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lessor_id", nullable = false)
    private Lessor lessor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lessee_id", nullable = false)
    private Lessee lessee;

    @Column(name = "email_sent", nullable = false)
    int isSent;




}
