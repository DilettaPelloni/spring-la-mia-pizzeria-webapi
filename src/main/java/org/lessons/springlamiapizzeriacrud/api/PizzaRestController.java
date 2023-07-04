package org.lessons.springlamiapizzeriacrud.api;

import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.RET;
import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.lessons.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin //per evitare CORS
@RequestMapping("/api/v1/pizzas")
public class PizzaRestController {

    @Autowired
    PizzaService pizzaService;

    //INDEX -------------------------------------------
    @GetMapping
    public List<Pizza> index(
        @RequestParam Optional<String> keyword
    ) {
        return pizzaService.getAll(keyword);
    }

    //SHOW -------------------------------------------
    @GetMapping("/{id}")
    public Pizza show(
        @PathVariable Integer id
    ) {
        try {
            return pizzaService.getById(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //CREATE -------------------------------------------
    @PostMapping
    public Pizza create(
        @Valid @RequestBody Pizza pizza
    ) {
        return pizzaService.create(pizza);
    }


}
