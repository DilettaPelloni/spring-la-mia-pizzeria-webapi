package org.lessons.springlamiapizzeriacrud.exceptions;

public class NameNotUniqueException extends RuntimeException{

    public NameNotUniqueException(String message) {
        super(message);
    }
}
