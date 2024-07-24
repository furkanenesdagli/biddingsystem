package com.example.biddingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double startingPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
