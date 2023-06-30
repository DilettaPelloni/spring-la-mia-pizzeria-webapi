package org.lessons.springlamiapizzeriacrud.controller;

import org.lessons.springlamiapizzeriacrud.model.Ingredient;
import org.lessons.springlamiapizzeriacrud.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping
    public String index(Model model) {
        List<Ingredient> ingredients = ingredientRepository.findAll(); //per mostrare l'elenco degli ingredienti
        model.addAttribute("ingObj", new Ingredient()); //per l'editor
        model.addAttribute("ingredients", ingredients);
        return "/ingredient/index";
    }

}
