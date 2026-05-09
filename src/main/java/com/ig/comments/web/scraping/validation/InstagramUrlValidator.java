package com.ig.comments.web.scraping.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstagramUrlValidator implements ConstraintValidator<ValidInstagramUrl, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (url == null || url.isBlank()) {
            context.buildConstraintViolationWithTemplate("a url não pode estar vazia.").addConstraintViolation();
            return false;
        }

        if (!url.contains("instagram.com")) {
            context.buildConstraintViolationWithTemplate("a url deve ser um link válido do instagram.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
