package com.apex.webserver.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue
    private Long id;
}
