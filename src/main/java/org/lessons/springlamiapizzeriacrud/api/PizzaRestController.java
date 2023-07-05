package org.lessons.springlamiapizzeriacrud.api;

import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.RET;
import org.lessons.springlamiapizzeriacrud.exceptions.NameNotUniqueException;
import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.lessons.springlamiapizzeriacrud.responses.ApiResponse;
import org.lessons.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin //per evitare CORS
@RequestMapping("/api/v1/pizzas")
public class PizzaRestController {

    @Autowired
    private PizzaService pizzaService;

    //INDEX -------------------------------------------
    @GetMapping
    public ResponseEntity<?> index(
        @RequestParam Optional<String> keyword
    ) {
        return new ResponseEntity<>(new ApiResponse<>(pizzaService.getAll(keyword)), HttpStatus.OK);
    }

    //SHOW -------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> show(
        @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(new ApiResponse<>(pizzaService.getById(id)), HttpStatus.OK);
        } catch (PizzaNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    //CREATE -------------------------------------------
    @PostMapping
    public ResponseEntity<?> create(
        @Valid @RequestBody Pizza pizza,
        BindingResult bindingResult
    ) {
        try {
            return new ResponseEntity<>(new ApiResponse<>(pizzaService.create(pizza, bindingResult)), HttpStatus.CREATED);
        } catch (InvalidAttributeValueException e) {
            return new ResponseEntity<>(new ApiResponse<>(null, bindingResult.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
    }

    //UPDATE -------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
        @PathVariable Integer id,
        @Valid @RequestBody Pizza pizza,
        BindingResult bindingResult
    ) {
        try {
            return new ResponseEntity<>(new ApiResponse<>(pizzaService.update(id, pizza, bindingResult)), HttpStatus.OK);
        } catch (PizzaNotFoundException e ) {
            return new ResponseEntity<>(new ApiResponse<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidAttributeValueException e) {
            return new ResponseEntity<>(new ApiResponse<>(null, bindingResult.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
    }

    //DELETE -------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
        @PathVariable Integer id
    ) {
        try {
            pizzaService.deleteById(id);
            return new ResponseEntity<>(new ApiResponse<>("Pizza with id "+id+" deleted"), HttpStatus.OK);
        } catch (PizzaNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
