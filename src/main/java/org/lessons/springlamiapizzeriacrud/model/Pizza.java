package org.lessons.springlamiapizzeriacrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.lessons.springlamiapizzeriacrud.annotations.UniquePizzaName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pizzas")
public class Pizza {

    //CHIAVE PRIMARIA ----------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //COLONNE ----------------------------------------------------------
    @Column(nullable = false, unique = true)
//    @UniquePizzaName
    @NotBlank(message = "Name must not be null or blank")
    @Size(min=3, max=255, message = "Name must have min 3 and max 255 characters")
    private String name;

    @Column(columnDefinition="TEXT") //ho cambiato il tipo di colonna in modo che fosse una text, per ospitare più di 255 caratteri
    @NotBlank(message = "Description must not be null or blank")
    @Size(min=3, max=1600, message = "Description must have min 3 and max 1600 characters")
    private String description;

    private String img;

    @Lob
    @Column(length = 16777215)
    private byte[] image;

    @PositiveOrZero(message = "Price must have a positive value")
    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    @OneToMany(mappedBy = "pizza")
    private List<Offer> offers = new ArrayList<>(); //la inizializzo per assicurarmi che non sia mai null

    @ManyToMany
    @JoinTable(
        name = "ingredient_pizza",
        joinColumns = @JoinColumn(name = "pizza_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();


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
    public List<Offer> getOffers() {
        return offers;
    }
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
}
