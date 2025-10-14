package com.tinonio.ai.demoAI.api;

import com.tinonio.ai.demoAI.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorViewAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public String onValidation(ValidationException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "fragments/error-card :: card";
    }
}
