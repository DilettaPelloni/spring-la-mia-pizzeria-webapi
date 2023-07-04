package org.lessons.springlamiapizzeriacrud.annotations;

import ch.qos.logback.core.model.INamedModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class UniquePizzaNameValidator implements ConstraintValidator<UniquePizzaName, String> {

    @Autowired
    PizzaRepository pizzaRepository;
    String table; //per passare parametri dall'annotation da usare in is valid

    @Override
    public void initialize(UniquePizzaName constraintAnnotation) {
        this.table = constraintAnnotation.table(); //per passare parametri dall'annotation da usare in is valid
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Pizza> result = pizzaRepository.findByName(s);
        return result.isEmpty();
    }
}
