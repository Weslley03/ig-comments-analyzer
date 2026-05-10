package com.ig.comments.web.scraping.dto;

public record ErrorResponseDTO(int status, String error, String message, String timestamp) {}
