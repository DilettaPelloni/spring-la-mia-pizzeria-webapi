package org.lessons.springlamiapizzeriacrud.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePizzaNameValidator.class) //indica la classe che contiene la logica di validazione
@Target( { ElementType.FIELD }) //dove posso mettere l'annotation
@Retention(RetentionPolicy.RUNTIME) //questa è un'annotation che serve anche quando il programma è in esecuzione
public @interface UniquePizzaName {
    String message() default "Name must be unique"; //messaggio di default
    String table() default "pizzas"; //prova di passaggio parametri
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
