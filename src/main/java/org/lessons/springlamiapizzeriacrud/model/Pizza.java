package org.lessons.springlamiapizzeriacrud.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pizzas")
public class Pizza {

    //CHIAVE PRIMARIA ----------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //COLONNE ----------------------------------------------------------
    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private String img;
    private BigDecimal price;


    //GETTERS E SETTERS ----------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() { return img; }

    public void setImg(String img) {
        this.img = img;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
