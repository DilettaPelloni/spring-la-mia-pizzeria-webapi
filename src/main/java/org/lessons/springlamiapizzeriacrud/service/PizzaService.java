package org.lessons.springlamiapizzeriacrud.service;

import org.lessons.springlamiapizzeriacrud.exceptions.NameNotUniqueException;
import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    //restituisce la lista delle pizze, intera o filtrata
    public List<Pizza> getAll(Optional<String> keyword) {
        if(keyword.isEmpty()) {
            return pizzaRepository.findAll();
        } else {
            return pizzaRepository.findByNameContainingIgnoreCase(keyword.get());
        }
    }

    //restituisce una pizza trovata per id. Se non la trova lancia PizzaNotFoundException
    public Pizza getById(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()) {
            return pizza.get();
        } else {
            throw new PizzaNotFoundException("Pizza with id "+id+" not found!");
        }
    }

    //crea una nuova pizza. Lancia un'eccezione se il campo nome è già presente a DB
    public Pizza create(Pizza pizza, BindingResult bindingResult) throws InvalidAttributeValueException{
        if(!isNameUnique(pizza.getName())) {
            bindingResult.addError(new FieldError(
                    "pizza",
                    "name",
                    pizza.getName(),
                    false,
                    null,
                    null,
                    "Name "+pizza.getName()+" is already in use"
            ));
        }
        if(bindingResult.hasErrors()) {
            throw new InvalidAttributeValueException();
        } else {
            return pizzaRepository.save(pizza);
        }
    }

    //modifica una pizza esistente
    public Pizza update(Integer id, Pizza pizza, BindingResult bindingResult) throws PizzaNotFoundException, InvalidAttributeValueException{
        Pizza oldPizza = getById(id);
        if(!oldPizza.getName().equals(pizza.getName()) && !isNameUnique(pizza.getName())) {
            bindingResult.addError(new FieldError(
                    "pizza",
                    "name",
                    pizza.getName(),
                    false,
                    null,
                    null,
                    "Name "+pizza.getName()+" is already in use"
            ));

        }
        if(bindingResult.hasErrors()) {
            throw new InvalidAttributeValueException();
        } else {
            pizza.setId(id);
            return pizzaRepository.save(pizza);
        }
    }

    //cancella una pizza esistente
    public void deleteById(Integer id) throws PizzaNotFoundException{
        Pizza pizza = getById(id);
        pizzaRepository.delete(pizza);
    }

    //verifica se i nome di una pizza esiste già a DB
    private boolean isNameUnique(String name){
        Optional<Pizza> result = pizzaRepository.findByName(name);
        return result.isEmpty();
    }

}
