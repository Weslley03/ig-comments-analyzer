package com.ig.comments.web.scraping.demo.dto;

import java.util.List;

public record InstagramPostDataDTO(
        String description,
        List<String> comments) {}
