package com.ig.comments.web.scraping.dto;

public class ResponseApiDTO<T> {
    private int status;
    private String message;
    private T response;

    public ResponseApiDTO(int status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    public ResponseApiDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getResponse() {
      return response;
    }
}

