package org.lessons.springlamiapizzeriacrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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
    @NotBlank(message = "Name must not be null or blank")
    @Size(min=3, max=255, message = "Name must have min 3 and max 255 characters")
    private String name;

    @Column(columnDefinition="TEXT") //ho cambiato il tipo di colonna in modo che fosse una text, per ospitare più di 255 caratteri
    @NotBlank(message = "Description must not be null or blank")
    @Size(min=3, max=255, message = "Description must have min 3 and max 1600 characters")
    private String description;

    @NotBlank(message = "Image must not be null or blank")
    private String img;

    @PositiveOrZero(message = "Price must have a positive value")
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
