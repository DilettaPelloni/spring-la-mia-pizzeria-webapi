package org.lessons.springlamiapizzeriacrud.controller;

import org.lessons.springlamiapizzeriacrud.model.Offer;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    PizzaRepository pizzaRepository;


    @GetMapping("/create")
    public String create(
        Model model,
        @RequestParam("pizzaId") Integer pizzaId
    ) {
        Offer offer = new Offer(); //nuova istanza di Offer
        Optional<Pizza> pizza = pizzaRepository.findById(pizzaId); //cerco la pizza di riferimento con l'id che ho ricevuto tramite parametro
        if(pizza.isEmpty()) { //se l'id non è valido lancio errore 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + " not found");
        }
        offer.setPizza(pizza.get()); //la metto dentro alla nuova istanza di Offer
        model.addAttribute("offer", offer);
        return "offer/editor";
    }

}
