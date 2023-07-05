package org.lessons.springlamiapizzeriacrud.controller;

import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("files")
public class FileController {

    @Autowired
    PizzaService pizzaService;

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPizzaPhoto(
        @PathVariable Integer id
    ) {
        try {
            Pizza pizza = pizzaService.getById(id);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pizza.getImage());
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id "+id+" not found");
        }
    }
}
