package org.lessons.springlamiapizzeriacrud.repository;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    //cerca le pizze il cui nome contiene una stringa ignorando maiuscole/minuscole
    List<Pizza> findByNameContainingIgnoreCase(String keyword);

}
