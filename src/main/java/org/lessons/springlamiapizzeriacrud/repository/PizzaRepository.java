package org.lessons.springlamiapizzeriacrud.repository;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    //cerca le pizze il cui nome contiene una stringa ignorando maiuscole/minuscole
    List<Pizza> findByNameContainingIgnoreCase(String keyword);
    Page<Pizza> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    //cerco una pizza tramite il nome(dato univoco)
    Optional<Pizza> findByName(String name);

}
