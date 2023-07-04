package org.lessons.springlamiapizzeriacrud.responses;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {

    private T content;
    private boolean success;
    private List<?> errors = new ArrayList<>();

    public ApiResponse(T content) {
        this.content = content;
        this.success = true;
    }
    public ApiResponse(T content, List<?> errors) {
        this.content = content;
        this.errors = errors;
        this.success = false;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }
}
