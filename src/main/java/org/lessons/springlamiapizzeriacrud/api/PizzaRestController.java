package org.lessons.springlamiapizzeriacrud.api;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin //per evitare CORS
@RequestMapping("/api/v1/pizzas")
public class PizzaRestController {

    @Autowired
    PizzaRepository pizzaRepository;

    @GetMapping
    public List<Pizza> index() {
        return pizzaRepository.findAll();
    }

}
