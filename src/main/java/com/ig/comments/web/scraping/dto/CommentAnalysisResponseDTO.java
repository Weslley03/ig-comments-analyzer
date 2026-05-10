package com.ig.comments.web.scraping.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record CommentAnalysisResponseDTO(
        Long id,
        String igUrl,
        String descriptionContent,
        String result,
        Map<String, Object> metadata,
        OffsetDateTime createdAt) {
}
