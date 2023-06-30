package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessage;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessageType;
import org.lessons.springlamiapizzeriacrud.model.Ingredient;
import org.lessons.springlamiapizzeriacrud.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("ingObj", new Ingredient()); //per l'editor
        List<Ingredient> ingredients = ingredientRepository.findAll(); //per mostrare l'elenco degli ingredienti
        model.addAttribute("ingredients", ingredients);
        return "/ingredient/index";
    }

    @PostMapping("/save")
    public String save(
        @Valid @ModelAttribute Ingredient formIngredient,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) { //se ci sono errori
            List<Ingredient> ingredients = ingredientRepository.findAll();
            model.addAttribute("ingredients", ingredients);
            return "/ingredient/index";
        }
        ingredientRepository.save(formIngredient);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Ingredient " + formIngredient.getName() + " created successfully!"));
        return "redirect:/ingredients";
    }

}
