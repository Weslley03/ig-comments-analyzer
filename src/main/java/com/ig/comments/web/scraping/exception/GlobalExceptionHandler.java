package com.ig.comments.web.scraping.exception;

import com.ig.comments.web.scraping.dto.ResponseApiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ResponseApiDTO<Void>> handleInvalidUrl(InvalidUrlException ex) {
        log.warn("invalid URL: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseApiDTO<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ScrapingException.class)
    public ResponseEntity<ResponseApiDTO<Void>> handleScraping(ScrapingException ex) {
        log.error("scraping failed: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ResponseApiDTO<>(HttpStatus.UNPROCESSABLE_CONTENT.value(), ex.getMessage()));
    }

    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<ResponseApiDTO<Void>> handleAiService(AiServiceException ex) {
        log.error("ai service error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ResponseApiDTO<>(HttpStatus.BAD_GATEWAY.value(), ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseApiDTO<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "parâmetro obrigatório ausente: '" + ex.getParameterName() + "'.";
        log.warn(message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseApiDTO<>(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApiDTO<Void>> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseApiDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "ocorreu um erro inesperado. por favor, tente novamente mais tarde."));
    }
}
