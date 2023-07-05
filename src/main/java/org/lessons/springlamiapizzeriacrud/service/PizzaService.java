package org.lessons.springlamiapizzeriacrud.service;

import org.lessons.springlamiapizzeriacrud.dto.PizzaDto;
import org.lessons.springlamiapizzeriacrud.exceptions.NameNotUniqueException;
import org.lessons.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    //READ ---------------------------------------------------------------------------------
    //restituisce la lista delle pizze, intera o filtrata
    public List<Pizza> getAll(Optional<String> keyword) {
        if(keyword.isEmpty()) {
            return pizzaRepository.findAll();
        } else {
            return pizzaRepository.findByNameContainingIgnoreCase(keyword.get());
        }
    }
    //restituisce la lista delle pizze paginata, intera o filtrata
    public Page<Pizza> getPage(
        Pageable pageable,
        Optional<String> keyword
    ) {
        if(keyword.isEmpty()) {
            return pizzaRepository.findAll(pageable);
        } else {
            return pizzaRepository.findByNameContainingIgnoreCase(keyword.get(), pageable);
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

    //restituisce un PizzaDto partendo da una Pizza trovata per id. Se non la trova lancia PizzaNotFoundException
    public PizzaDto getPizzaDtoById(Integer id) throws  PizzaNotFoundException{
        Pizza pizza = getById(id);
        return mapPizzaToPizzaDto(pizza);
    }

    //CREATE ---------------------------------------------------------------------------------
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

    //crea una nuova pizza partendo da un PizzaDto
    public Pizza create(PizzaDto pizzaDto, BindingResult bindingResult) throws InvalidAttributeValueException{
        Pizza pizza = mapPizzaDtoToPizza(pizzaDto);
        return create(pizza, bindingResult);
    }

    //UPDATE ---------------------------------------------------------------------------------
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
            pizza.setImg(null);
            return pizzaRepository.save(pizza);
        }
    }

    //modifica una pizza esistente partendo da un PizzaDto
    public Pizza update(Integer id, PizzaDto pizzaDto, BindingResult bindingResult) throws PizzaNotFoundException, InvalidAttributeValueException{
        Pizza pizza = mapPizzaDtoToPizza(pizzaDto);
        return update(id, pizza, bindingResult);
    }

    //DELETE ---------------------------------------------------------------------------------
    //cancella una pizza esistente
    public void deleteById(Integer id) throws PizzaNotFoundException{
        Pizza pizza = getById(id);
        pizzaRepository.delete(pizza);
    }

    //UTILITY -------------------------------------------------------------------------------------

    //verifica se i nome di una pizza esiste già a DB
    private boolean isNameUnique(String name){
        Optional<Pizza> result = pizzaRepository.findByName(name);
        return result.isEmpty();
    }

    //trasforma un PizzaDTO in una Pizza
    private Pizza mapPizzaDtoToPizza(PizzaDto pizzaDto){
        Pizza pizza = new Pizza();
        pizza.setName(pizzaDto.getName());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPrice(pizzaDto.getPrice());
        pizza.setIngredients(pizzaDto.getIngredients());
        pizza.setImage(multipartFileToByteArray(pizzaDto.getImage()));
        return pizza;
    }

    //trasforma una Pizza in un PizzaDto
    private PizzaDto mapPizzaToPizzaDto(Pizza pizza){
        PizzaDto result = new PizzaDto();
        result.setId(pizza.getId());
        result.setName(pizza.getName());
        result.setDescription(pizza.getDescription());
        result.setPrice(pizza.getPrice());
        result.setImg(pizza.getImg());
        result.setIngredients(pizza.getIngredients());
        return result;
    }

    //converte un MultipartFile in un byte[]
    private byte[] multipartFileToByteArray(MultipartFile mpf){
        byte[] bytes = null;
        if(mpf != null && !mpf.isEmpty()) {
            try {
                bytes = mpf.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

}
