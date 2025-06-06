package com.apex.webserver.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.apex.webserver.model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
// @Table(name = "accounts")
public class Account {
    @Id @GeneratedValue
    private Long id;
    private Long code;
    private String name;
    private AccountType type;
    private String category;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}


