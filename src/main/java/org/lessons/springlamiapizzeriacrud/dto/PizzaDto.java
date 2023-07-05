package org.lessons.springlamiapizzeriacrud.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.lessons.springlamiapizzeriacrud.model.Ingredient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PizzaDto {
    //ID
    private Integer id;
    //NAME
    @NotBlank(message = "Name must not be null or blank")
    @Size(min=3, max=255, message = "Name must have min 3 and max 255 characters")
    private String name;
    //DESCRIPTION
    @NotBlank(message = "Description must not be null or blank")
    @Size(min=3, max=1600, message = "Description must have min 3 and max 1600 characters")
    private String description;
    //IMAGE
    @Lob
    @Column(length = 16777215)
    private byte[] image;
    //PRICE
    @PositiveOrZero(message = "Price must have a positive value")
    @NotNull(message = "Price must not be null")
    private BigDecimal price;
    private List<Ingredient> ingredients = new ArrayList<>();

    //GETTERS & SETTERS
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
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
