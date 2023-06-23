package org.lessons.springlamiapizzeriacrud.controller;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping
    public String index(Model model) {
        //prendo la lista di pizze
        List<Pizza> pizzas = pizzaRepository.findAll();
        //passo la lista al model
        model.addAttribute("pizzas", pizzas);
        //ritorno la view
        return "pizza/index";
    }

    @GetMapping("/{id}")
    public String show(
        Model model,
        @PathVariable("id") Integer id
    ) {
        Optional<Pizza> pizza = pizzaRepository.findById(id); //cerco la pizza in base all'id
        if(pizza.isPresent()) { //se ho trovato qualcosa
            model.addAttribute("pizza", pizza.get()); //do la pizza alla view
        } else { //altrimenti lancio un eccezione che restituirà un errore 404
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, //tipo di errore
                "Pizza with id " + id + " not found" //messaggio
            );
        }
        return "/pizza/show";
    }

}
