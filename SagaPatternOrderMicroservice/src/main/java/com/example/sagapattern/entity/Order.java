package com.example.sagapattern.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String item;
    @Column
    private int quality;
    @Column
    private double amount;
    @Column
    private String status;
}
