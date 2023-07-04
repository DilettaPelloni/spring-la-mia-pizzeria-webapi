package org.lessons.springlamiapizzeriacrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Title must not be null or blank")
    @Size(min=3, max=255, message = "Name must have min 3 and max 255 characters")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "Start date must not be null")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "End date must not be null")
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="pizza_id", nullable = false)
    private Pizza pizza;

    //GETTERS & SETTERS -------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public Pizza getPizza() {
        return pizza;
    }
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }
}
