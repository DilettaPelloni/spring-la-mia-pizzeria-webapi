package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.dto.PizzaDto;
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
    private PizzaRepository pizzaRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    //READ --------------------------------------------------------------------------------------------------------------
    @GetMapping
    public String index(
        Model model,
        @RequestParam(name= "keyword", required = false) String keyword //gestisco la presenza di un eventuale parametro keyword nella richiesta
    ) {
        List<Pizza> pizzas; //variabile da restituire
        if(keyword == null || keyword.isBlank()) { //se il parametro non c'è o è una stringa vuota
            pizzas = pizzaRepository.findAll(); //restituirò l'intera lista di pizze
        } else { //se mi è stata data la keyword da ricercare
            pizzas = pizzaRepository.findByNameContainingIgnoreCase(keyword); //la passo al metodo che la utilizzerà per fare la QUERY
            model.addAttribute("keyword", keyword); //passo la keyword, in modo da poterla mostrare in pagina
        }
        model.addAttribute("pizzas", pizzas); //passo la lista al model
        return "pizza/index"; //ritorno la view
    }

    @GetMapping("/{id}")
    public String show(
        Model model,
        @PathVariable("id") Integer id
    ) {
        Pizza pizza = getPizzaById(id); //uso un metodo che cerca la pizza per id e me la restituisce, se non la trova lancia un'eccezione
        model.addAttribute("pizza", pizza); //se ho trovato qualcosa do la pizza alla view
        return "/pizza/show";
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
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza " + formPizza.getName() + " created successfully!"));
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
        Pizza pizza = getPizzaById(id);
        model.addAttribute("ingList", ingredientRepository.findAll());
        model.addAttribute(pizza);
        return "/pizza/editor";
    }

    @PostMapping("/edit/{id}")
    public String update(
        @PathVariable("id") Integer id,
        @Valid @ModelAttribute("pizza") Pizza formPizza,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        Pizza oldPizza = getPizzaById(id); //vado a prendere la pizza prima della modifica dal DB
        if(!oldPizza.getName().equals(formPizza.getName()) && !isNameUnique(formPizza.getName())) { //se il nome è stato modificato E NON è univoco
            bindingResult.addError(new FieldError( //creo un nuovo errore
                    "pizza",
                    "name",
                    formPizza.getName(),
                    false,
                    null,
                    null,
                    "Name must be unique."
            ));
        }
        if(bindingResult.hasErrors()) { //se ho trovato errori rimango sull'editor
            model.addAttribute("ingList", ingredientRepository.findAll());
            return "/pizza/editor";
        }
        formPizza.setId(oldPizza.getId()); //imposto l'id della pizza aggiornata uguale all'id originale
        pizzaRepository.save(formPizza);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza " + formPizza.getName() + " updated successfully!"));
        return "redirect:/pizzas/" + formPizza.getId();
    }

    //DELETE --------------------------------------------------------------------------------------------------------------
    @PostMapping("/delete/{id}")
    public String delete(
        @PathVariable("id") Integer id,
        RedirectAttributes redirectAttributes
    ) {
        Pizza pizza = getPizzaById(id);
        pizzaRepository.delete(pizza);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza " + pizza.getName() + " deleted successfully!"));
        return "redirect:/pizzas";
    }

    //UTILITIES ----------------------------------------------------------------------------------------
    private boolean isNameUnique(String name){
        Optional<Pizza> result = pizzaRepository.findByName(name);
        return result.isEmpty();
    }
    private Pizza getPizzaById(Integer id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id); //cerco la pizza in base all'id
        if(pizza.isEmpty()) { // se non la trovo lancio un'eccezione che restituirà un errore 404
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, //tipo di errore
                    "Pizza with id " + id + " not found" //messaggio
            );
        }
        return pizza.get(); //se la trovo, restituisco la pizza
    }


}
