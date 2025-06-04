package com.apex.webserver.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    // private TransactionStatus status;
}
