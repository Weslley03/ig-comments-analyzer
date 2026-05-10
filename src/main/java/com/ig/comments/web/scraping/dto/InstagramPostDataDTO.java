package com.ig.comments.web.scraping.dto;

import java.util.List;

public record InstagramPostDataDTO(
        String description,
        List<String> comments) {}
