package org.lessons.springlamiapizzeriacrud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



}
