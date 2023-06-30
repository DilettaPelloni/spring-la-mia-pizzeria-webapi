package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessage;
import org.lessons.springlamiapizzeriacrud.messages.AlertMessageType;
import org.lessons.springlamiapizzeriacrud.model.Ingredient;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public String index(
        @RequestParam(value = "edit") Optional<Integer> ingId, //se accedo a questa rotta dal pulsante edit passo un parametro con l'id dell'ingrediente da modificare
        Model model
    ) {
        Ingredient ingObj; //preparo la variabile che conterrà l'ingrediente da passare al form
        if(ingId.isPresent()) { //se mi è stato passato il parametro
            Optional<Ingredient> result = ingredientRepository.findById(ingId.get()); //cerco l'ingrediente a DB
            if(result.isPresent()) { //se lo trovo lo passo alla variabile
                ingObj = result.get();
            } else { //se non lo trovo, passo un'istanza vuota
                    ingObj = new Ingredient();
            }
        } else { //se non mi sono stati dati parametri, passo un'istanza vuota
            ingObj = new Ingredient();
        }
        model.addAttribute("ingObj", ingObj); //per l'editor
        List<Ingredient> ingredients = ingredientRepository.findAll(); //per mostrare l'elenco degli ingredienti
        model.addAttribute("ingredients", ingredients);
        return "/ingredient/index";
    }

    @PostMapping("/save")
    public String save(
        @Valid @ModelAttribute("ingObj") Ingredient formIngredient,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if(!isNameUnique(formIngredient.getName())) { //se il nome NON è univoco
            bindingResult.addError(new FieldError(
                    "ingObj",
                    "name",
                    formIngredient.getName(),
                    false,null,null,
                    "Name must be unique."
            ));
        }
        if(bindingResult.hasErrors()) { //se ci sono errori
            List<Ingredient> ingredients = ingredientRepository.findAll();
            model.addAttribute("ingredients", ingredients);
            return "/ingredient/index";
        }
        //testo per messaggio
        String action ="created";
        if(formIngredient.getId() != null) { action = "updated"; }
        ingredientRepository.save(formIngredient);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Ingredient "+formIngredient.getName()+" "+action+" successfully!"));
        return "redirect:/ingredients";
    }

    @PostMapping("delete/{id}")
    public String delete(
        @PathVariable Integer id,
        RedirectAttributes redirectAttributes
    ) {
        Ingredient ingredient = getIngredientById(id);
        ingredientRepository.delete(ingredient);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Ingredient "+ingredient.getName()+" deleted successfully!"));
        return "redirect:/ingredients";
    }

    //UTILITIES ----------------------------------------------------------------------------------------
    private boolean isNameUnique(String name){
        Optional<Ingredient> result = ingredientRepository.findByName(name);
        return result.isEmpty();
    }
    private Ingredient getIngredientById(Integer id) {
        Optional<Ingredient> result = ingredientRepository.findById(id);
        if(result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ingredient with id " + id + " not found");
        }
        return result.get();
    }

}
