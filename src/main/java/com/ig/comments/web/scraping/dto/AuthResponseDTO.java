package com.ig.comments.web.scraping.dto;

import java.util.UUID;

public record AuthResponseDTO(String token, UUID userId) {}
