package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

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
        model.addAttribute("pizza", new Pizza());
        return "pizza/create";
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("pizza") Pizza formPizza,
        BindingResult bindingResult
    ) {
        if(!isNameUnique(formPizza.getName())) { //se il nome NON è univoco
            bindingResult.addError(new FieldError(
                    "pizza", //il nome dell'oggetto a cui si riferisce l'errore
                    "name", //l'attributo dell'oggetto a cui si riferisce l'errore
                    formPizza.getName(), //il valore sbagliato
                    false, //true se è un binding failure, false se è un validation failure
                    null, //array di codici dell'errore
                    null, //array di argomenti dell'errore
                    "Name must be unique." //messaggio d'errore
            ));
        }
        if(bindingResult.hasErrors()) {
            return "/pizza/create";
        }
        pizzaRepository.save(formPizza);
        return "redirect:/pizzas/" + formPizza.getId();
    }

    //UPDATE --------------------------------------------------------------------------------------------------------------
    @GetMapping("/edit/{id}")
    private String edit(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Pizza pizza = getPizzaById(id);
        model.addAttribute(pizza);
        return "/pizza/editor";
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
