package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessage;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessageType;
import org.lessons.springlamiapizzeriacrud.model.Offer;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.OfferRepository;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OfferRepository offerRepository;

    //CREATE ------------------------------------------------------------------------------
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

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("offer") Offer formOffer,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "offer/editor";
        }
        offerRepository.save(formOffer);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Offer " + formOffer.getTitle() + " created successfully!"));
        return "redirect:/pizzas/" + formOffer.getPizza().getId();
    }

    //UPDATE ------------------------------------------------------------------------------
    @GetMapping("/edit/{id}")
    public String edit(
        @PathVariable("id") Integer id,
        Model model
    ) {
        Offer offer = getOfferById(id);
        model.addAttribute("offer", offer);
        return "offer/editor";
    }

    @PostMapping("/edit/{id}")
    public String update(
        @PathVariable("id") Integer id,
        @Valid @ModelAttribute("offer") Offer formOffer,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "offer/editor";
        }
        formOffer.setId(id);
        offerRepository.save(formOffer);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Offer " + formOffer.getTitle() + " updated successfully!"));
        return "redirect:/pizzas/" + formOffer.getPizza().getId();
    }

    //DELETE ------------------------------------------------------------------------------
    @PostMapping("delete/{id}")
    public String delete(
        @PathVariable("id") Integer id,
        RedirectAttributes redirectAttributes
    ) {
        Offer offer = getOfferById(id);
        offerRepository.delete(offer);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Offer " + offer.getTitle() + " deleted successfully!"));
        return "redirect:/pizzas/" + offer.getPizza().getId();
    }

    //UTILITY ------------------------------------------------------------------------------
    private Offer getOfferById(Integer id) {
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer with id " + id + " not found");
        }
        return offer.get();
    }

}
