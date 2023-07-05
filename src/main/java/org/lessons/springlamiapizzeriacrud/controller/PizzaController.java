package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.dto.PizzaDto;
import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessage;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessageType;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.IngredientRepository;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.lessons.springlamiapizzeriacrud.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;
    @Autowired
    private IngredientRepository ingredientRepository;

    //READ --------------------------------------------------------------------------------------------------------------
    @GetMapping
    public String index(
        Model model,
        @RequestParam Optional<String> keyword
    ) {
        model.addAttribute("pizzas", pizzaService.getAll(keyword));
        return "pizza/index";
    }

    @GetMapping("/{id}")
    public String show(
        Model model,
        @PathVariable("id") Integer id
    ) {
        try {
            model.addAttribute("pizza", pizzaService.getById(id));
            return "/pizza/show";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

    //CREATE --------------------------------------------------------------------------------------------------------------
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ingList", ingredientRepository.findAll());
        model.addAttribute("pizza", new PizzaDto());
        return "/pizza/editor";
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("pizza") PizzaDto formPizza,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        try {
            Pizza pizza = pizzaService.create(formPizza, bindingResult);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza " + pizza.getName() + " created successfully!"));
            return "redirect:/pizzas/" + pizza.getId();
        } catch (InvalidAttributeValueException e) {
            model.addAttribute("ingList", ingredientRepository.findAll());
            return "/pizza/editor";
        }
    }

    //UPDATE --------------------------------------------------------------------------------------------------------------
    @GetMapping("/edit/{id}")
    public String edit(
        @PathVariable("id") Integer id,
        Model model
    ) {
        try {
            PizzaDto pizzaDto = pizzaService.getPizzaDtoById(id);
            model.addAttribute("ingList", ingredientRepository.findAll());
            model.addAttribute("pizza", pizzaDto);
            return "/pizza/editor";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id "+id+" not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(
        @PathVariable("id") Integer id,
        @Valid @ModelAttribute("pizza") PizzaDto formPizza,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        try {
            Pizza pizza = pizzaService.update(id, formPizza, bindingResult);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza " + pizza.getName() + " updated successfully!"));
            return "redirect:/pizzas/" + pizza.getId();
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id "+id+" not found");
        } catch (InvalidAttributeValueException e) {
            model.addAttribute("ingList", ingredientRepository.findAll());
            return "/pizza/editor";
        }
    }

    //DELETE --------------------------------------------------------------------------------------------------------------
    @PostMapping("/delete/{id}")
    public String delete(
        @PathVariable("id") Integer id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            pizzaService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza with id "+id+" deleted successfully!"));
            return "redirect:/pizzas";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id "+id+" not found");
        }
    }

}
