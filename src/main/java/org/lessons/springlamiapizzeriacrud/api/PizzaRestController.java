package org.lessons.springlamiapizzeriacrud.api;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.lessons.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin //per evitare CORS
@RequestMapping("/api/v1/pizzas")
public class PizzaRestController {

    @Autowired
    PizzaService pizzaService;

    @GetMapping
    public List<Pizza> index(
        @RequestParam Optional<String> keyword
    ) {
        return pizzaService.getAll(keyword);
    }

}
